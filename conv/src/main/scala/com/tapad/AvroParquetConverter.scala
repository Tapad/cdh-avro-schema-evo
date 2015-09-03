package com.tapad

import com.twitter.scalding._
import com.twitter.scalding.avro.PackedAvroSource
import com.tapad.scalding.parquet.ParquetAvroSource

class AvroParquetConverter(args: Args) extends Job(args) {

  val inputPath = args("input")

  val outputPath = args("output")

  val records = PackedAvroSource[Example](inputPath)

  records.write(ParquetAvroSource[Example](outputPath))
}
