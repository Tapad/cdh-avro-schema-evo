package com.tapad

import com.tapad.util.DataGenerationUtils._

object DataGenerator extends Executable[Example] {

  def createRandomRecord: Example = {
    Example.newBuilder
      .setId(random.nextLong)
      .setMyEnum(ExampleEnum.values.apply(Math.abs(random.nextInt % 3)))
      .build()
  }
}
