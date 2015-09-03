package com.tapad

import com.tapad.util.DataGenerationUtils._

object DataGenerator extends Executable[Example] {

  def createRandomRecord: Example = {
    val builder = Example.newBuilder.setId(random.nextLong)
    if (random.nextBoolean) builder.setMyEnum(ExampleEnum.values.apply(Math.abs(random.nextInt % 3)))
    builder.build()
  }
}
