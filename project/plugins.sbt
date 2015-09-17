addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.11.2")

addSbtPlugin("com.tapad" % "sbt-jarjar" % "1.0.1")

addSbtPlugin("com.cavorite" % "sbt-avro" % "0.3.2")

dependencyOverrides += "org.apache.avro" % "avro" % "1.7.6"

dependencyOverrides += "org.apache.avro" % "avro-compiler" % "1.7.6"
