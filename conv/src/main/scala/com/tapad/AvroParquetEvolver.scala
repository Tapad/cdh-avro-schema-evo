package com.tapad

import com.twitter.scalding._
import com.tapad.scalding.parquet.ParquetAvroSource

class AvroParquetEvolver(args: Args) extends Job(args) {

  val inputPath = args("input")

  val outputPath = args("output")

  val records = ParquetAvroSource[Example](inputPath)

  records.write(ParquetAvroSource[Example](outputPath))
}
