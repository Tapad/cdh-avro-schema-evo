package com.tapad.scalding.parquet

import scala.reflect.ClassTag
import cascading.flow.FlowProcess
import cascading.scheme.{Scheme, SinkCall, SourceCall}
import cascading.tap.Tap
import cascading.tuple.Fields
import com.twitter.scalding._
import com.twitter.scalding.avro.AvroSchemaType
import org.apache.avro.Schema
import org.apache.avro.specific.SpecificRecord
import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.mapred.{JobConf, OutputCollector, RecordReader}
import org.apache.hadoop.util.StringUtils
import parquet.avro.{AvroReadSupport, AvroWriteSupport}
import parquet.filter2.predicate.FilterPredicate
import parquet.hadoop.mapred.{Container, DeprecatedParquetInputFormat, DeprecatedParquetOutputFormat}
import parquet.hadoop.{ParquetInputFormat, ParquetOutputFormat}

object ParquetAvroSource {

  def apply[A <: SpecificRecord : Manifest : AvroSchemaType : TupleConverter : TupleSetter]
    (source: String): ParquetAvroSource[A] =
    new ParquetAvroSource[A](
      Seq(source),
      schema = getSchema[A],
      projection = None,
      predicate = None
    )

  def project[A <: SpecificRecord : Manifest : AvroSchemaType : TupleConverter : TupleSetter]
    (source: String, projection: Schema): ParquetAvroSource[A] =
    new ParquetAvroSource[A](
      Seq(source),
      schema = getSchema[A],
      projection = Some(projection),
      predicate = None
    )

  def filter[A <: SpecificRecord : Manifest : AvroSchemaType : TupleConverter : TupleSetter]
    (source: String, predicate: FilterPredicate): ParquetAvroSource[A] =
    new ParquetAvroSource[A](
      Seq(source),
      schema = getSchema[A],
      projection = None,
      predicate = Some(predicate)
    )

  def projectAndFilter[A <: SpecificRecord : Manifest : AvroSchemaType : TupleConverter : TupleSetter]
    (source: String, projection: Schema, predicate: FilterPredicate): ParquetAvroSource[A] =
    new ParquetAvroSource[A](
      Seq(source),
      schema = getSchema[A],
      projection = Some(projection),
      predicate = Some(predicate)
    )

  def getSchema[A <: SpecificRecord : ClassTag] : Schema = {
    val mf = implicitly[ClassTag[A]]
    mf.runtimeClass.getMethod("getClassSchema").invoke(mf.runtimeClass).asInstanceOf[Schema]
  }
}

class ParquetAvroSource[A <: SpecificRecord : Manifest : AvroSchemaType: TupleConverter : TupleSetter](
    paths: Seq[String],
    schema: Schema,
    projection: Option[Schema],
    predicate: Option[FilterPredicate])
  extends FixedPathSource(paths: _*)
  with Mappable[A]
  with TypedSink[A]  {

  override def hdfsScheme = HadoopSchemeInstance(new ParquetAvroScheme(schema, projection, predicate).asInstanceOf[Scheme[_, _, _, _, _]])

  override def converter[U >: A] = TupleConverter.asSuperConverter[A, U](implicitly[TupleConverter[A]])

  override def setter[U <: A] = TupleSetter.asSubSetter[A, U](implicitly[TupleSetter[A]])
}

/** a Parquet Avro Scheme
  *
  * @constructor
  * @param sinkField the field to serialize as an avro record.
  * Normally this is the first (and only) field in a pipe, but it may be some other field if using a ParquetAvroTemplatedSource.
  **/
class ParquetAvroScheme[A](
    schema: Schema,
    requestedProjection: Option[Schema] = None,
    requestedPredicate: Option[FilterPredicate] = None,
    sinkField: Fields = Fields.FIRST)
  extends Scheme[JobConf, RecordReader[NullWritable, Container[A]], OutputCollector[NullWritable, A], Array[AnyRef], Array[AnyRef]] {

  val schemaName = schema.getName
  val schemaString = schema.toString
  val projectionString = requestedProjection.map(_.toString)

  setSinkFields(sinkField)
  setSourceFields(Fields.FIRST)

  override def retrieveSourceFields(flowProcess: FlowProcess[JobConf], tap: Tap[_, _, _]): Fields = {
    setSourceFields(new Fields(schemaName))
    super.retrieveSourceFields(flowProcess, tap)
  }

  override def sourceConfInit(flowProcess: FlowProcess[JobConf], tap: Tap[JobConf, RecordReader[NullWritable, Container[A]], OutputCollector[NullWritable, A]], conf: JobConf): Unit = {
    // set this constant as defined in AvroReadSupport.java (must be done before assigning the ReadSupportClass iff using EvolvableAvroReadSupport)
    conf.set("parquet.avro.read.schema", schemaString)
    ParquetInputFormat.setReadSupportClass(conf, classOf[AvroReadSupport[A]])
    conf.setInputFormat(classOf[DeprecatedParquetInputFormat[A]])
    projectionString.foreach(conf.set("parquet.avro.projection", _))
    requestedPredicate.foreach(ParquetInputFormat.setFilterPredicate(conf, _))
  }

  def source(flowProcess: FlowProcess[JobConf], sourceCall: SourceCall[Array[AnyRef], RecordReader[NullWritable, Container[A]]]): Boolean = {
    val value : Container[A] = sourceCall.getInput.createValue()
    if (!sourceCall.getInput().next(null, value)) {
      false
    } else {
      if (value == null) true
      else {
        val t = sourceCall.getIncomingEntry().getTuple
        t.clear()
        t.add(value.get())
        true
      }
    }
  }

  def sinkConfInit(flowProcess: FlowProcess[JobConf], tap: Tap[JobConf, RecordReader[NullWritable, Container[A]], OutputCollector[NullWritable, A]], conf: JobConf): Unit = {
    conf.set(ParquetOutputFormat.WRITE_SUPPORT_CLASS, classOf[AvroWriteSupport].getName)
    conf.setOutputFormat(classOf[DeprecatedParquetOutputFormat[A]])
    // These properties are declared in AvroWriteSupport.java
    conf.set("parquet.avro.schema", schemaString)
    conf.set("parquet.compression", "SNAPPY")
    val defaultBlockSize = 128L * 1024 * 1024
    val dfsBlockSize = (Option(conf.get("dfs.blocksize")) orElse Option(conf.get("dfs.block.size")))
      .map(_.trim)
      .map(StringUtils.TraditionalBinaryPrefix.string2long)
      .getOrElse(defaultBlockSize)
    val parquetBlockSize = (dfsBlockSize * 0.95).toLong
    conf.set("parquet.block.size", parquetBlockSize.toString)
  }

  override def sinkPrepare(flowProcess: FlowProcess[JobConf], sinkCall: SinkCall[Array[AnyRef], OutputCollector[NullWritable, A]]): Unit = {
    sinkCall.setContext(Array[AnyRef](new Schema.Parser().parse(schemaString)))
  }

  def sink(flowProcess: FlowProcess[JobConf], sinkCall: SinkCall[Array[AnyRef], OutputCollector[NullWritable, A]]): Unit = {
    val tupleEntry = sinkCall.getOutgoingEntry()
    val schema = sinkCall.getContext()(0).asInstanceOf[Schema]
    val record : A = tupleEntry.getObject(0).asInstanceOf[A]
    sinkCall.getOutput.collect(null, record)
  }
}
