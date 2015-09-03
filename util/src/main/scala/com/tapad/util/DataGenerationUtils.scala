package com.tapad.util

import java.io.File
import scala.reflect.ClassTag
import org.apache.avro.specific.{SpecificDatumWriter, SpecificRecord}
import org.apache.avro.file.DataFileWriter
import org.apache.avro.Schema

object DataGenerationUtils {

  val random = new java.util.Random

  val tempDirectory = new File(System.getProperty("java.io.tmpdir"))

  def createTempDirectory: File = {
    val randomName = java.lang.Integer.toHexString(random.nextInt)
    val f = new File(tempDirectory, randomName)
    createDirectory(f)
    f
  }

  def createDirectory(dir: File): Unit = {
    val msg = s"Could not create directory $dir"
    var tryCount = 0
    while (!dir.exists && !dir.mkdirs() && tryCount < 100) { tryCount += 1 }
    if (dir.isDirectory) {
      /* no-op */
    } else if (dir.exists) {
      sys.error(s"$msg: file exists and is not a directory.")
    } else {
      sys.error(msg)
    }
  }

  def createAvroDataFile[A](dir: File, schema: Schema, records: Seq[A]): File = {
    val file = File.createTempFile("example-", ".avro", dir)
    val datumWriter = new SpecificDatumWriter[A](schema)
    val fileWriter = new DataFileWriter[A](datumWriter)
    fileWriter.create(schema, file)
    records.foreach(fileWriter.append)
    fileWriter.close()
    file
  }

  def createAvroDataFileFromCliArgs[A <: SpecificRecord : ClassTag](args: Array[String])(factory: Int => Seq[A]): Unit = {
    val ctx = implicitly[ClassTag[A]]
    val schema = ctx.runtimeClass.getMethod("getClassSchema").invoke(ctx.runtimeClass).asInstanceOf[Schema]
    val n = args(1).toInt
    val records = factory(n)
    val dir = new File(args(0))
    createDirectory(dir)
    val file = createAvroDataFile[A](dir, schema, records)
    println(s"Avro data file $file created with $n records")
  }

  abstract class Executable[A <: SpecificRecord : ClassTag] {

    def main(args: Array[String]): Unit = {
      createAvroDataFileFromCliArgs(args)(Seq.fill(_)(createRandomRecord))
    }

    def createRandomRecord: A
  }
}
