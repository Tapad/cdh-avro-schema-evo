package com.tapad

import com.twitter.scalding._
import com.twitter.scalding.avro.PackedAvroSource

class AvroEvolver(args: Args) extends Job(args) {

  val inputPath = args("input")

  val outputPath = args("output")

  val records = PackedAvroSource[Example](inputPath)

  records.write(PackedAvroSource[Example](outputPath))
}
