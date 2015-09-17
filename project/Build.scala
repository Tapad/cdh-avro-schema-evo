import sbt._
import Keys._
import sbtassembly.Plugin._
import AssemblyKeys._
import sbtavro.SbtAvro
import SbtAvro.{avroConfig, avroSettings, stringType}
import sbtjarjar._
import JarJarPlugin._

object ProjectBuild extends Build {

  val ScalaVersion = "2.10.5"

  val CdhVersion = "cdh5.4.2"

  val HadoopVersion = s"2.6.0-mr1-$CdhVersion"

  val HadoopCommonVersion = s"2.6.0-$CdhVersion"

  val ScaldingVersion = "0.13.1"

  val CascadingVersion = "2.6.1"

  val AvroVersion = "1.7.6"

  val ParquetVersion = "1.6.0"

  val commonSettings = Seq(
    organization := "com.tapad",
    scalaVersion := ScalaVersion,
    scalacOptions := Seq("-deprecation", "-language:_"),
    resolvers ++= Seq(
      "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
      "Concurrent Maven Repository" at "http://conjars.org/repo",
      "Maven Central Repository" at "http://repo1.maven.org/maven2",
      "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository"
    )
  )

  lazy val root = Project("root", file(".")).aggregate(util, schema1, schema2, schema3, schema4, schema5, converter)

  lazy val util = Project(
    "util",
    file("util"),
    settings = Seq(
      libraryDependencies += "org.apache.avro" % "avro" % AvroVersion
    )
  )

  // init schema
  lazy val schema1 = schemaProject("schema-1")

  // add an enum value
  lazy val schema2 = schemaProject("schema-2")

  // remove an optional field
  lazy val schema3 = schemaProject("schema-3")

  // make enum optional
  lazy val schema4 = schemaProject("schema-4")

  // add an additional optional field
  lazy val schema5 = schemaProject("schema-5")

  lazy val converter = Project(
    "conv",
    file("conv"),
    settings = assemblySettings ++ jarjarSettings ++ Seq(
      libraryDependencies ++= Seq(
        "org.apache.hadoop"   % "hadoop-core"     % HadoopVersion,
        "org.apache.hadoop"   % "hadoop-common"   % HadoopCommonVersion,
        "com.twitter"        %% "scalding-core"   % ScaldingVersion,
        "com.twitter"        %% "scalding-avro"   % ScaldingVersion,
        "cascading"           % "cascading-core"  % CascadingVersion,
        "com.twitter"         % "parquet-hadoop"  % ParquetVersion,
        "com.twitter"         % "parquet-avro"    % ParquetVersion,
        "com.twitter"        %% "parquet-scala"   % ParquetVersion,
        "org.apache.avro"     % "avro-mapred"     % AvroVersion,
        "org.xerial.snappy"   % "snappy-java"     % "1.0.5"
      ),
      excludedJars in assembly <<= (fullClasspath in assembly) map { cp =>
        val blacklist = Set(
          "hamcrest-core-1.1.jar",
          "hsqldb-1.8.0.10.jar",
          "jasper-compiler-5.5.23.jar",
          "jasper-runtime-5.5.23.jar",
          "jetty-6.1.26.jar",
          "jsp-api-2.1.jar",
          "junit-4.10.jar",
          "servlet-api-2.5.jar",
          "servlet-api-2.5-20081211.jar",
          "snappy-java-1.0.5.jar"
        )
        cp.filter { jar => blacklist.contains(jar.data.getName) }
      },
      mergeStrategy in assembly <<= (mergeStrategy in assembly) { dedup =>
        {
          case fp if fp.endsWith("pom.properties") => MergeStrategy.discard
          case fp if fp.endsWith("pom.xml") => MergeStrategy.discard
          case fp if fp.endsWith(".class") => MergeStrategy.last
          case fp if fp.endsWith(".html") => MergeStrategy.discard
          case fp if fp.endsWith("netty.versions.properties") => MergeStrategy.discard
          case fp if fp.endsWith("ASL2.0") => MergeStrategy.discard
          case fp => dedup(fp)
        }
      },
      jarName in assembly := "conv.jar",
      mainClass in (Compile, run) := Some("com.twitter.scalding.Tool"),
      mainClass in assembly := Some("com.twitter.scalding.Tool"),
      jarjar <<= jarjar dependsOn assembly,
      JarJarKeys.jarName in jarjar <<= jarName in assembly,
      JarJarKeys.rules in jarjar := Seq(
        s"rule parquet.** parquet.jarjar.@1"
      )
    )
  ).dependsOn(util, schema1 % "provided")

  def schemaProject(name: String) = Project(
    name,
    file(name),
    settings = avroSettings ++ assemblySettings ++ Seq(
      sourceDirectory in avroConfig <<= (sourceDirectory in Compile)(_ / "avro"),
      javaSource in avroConfig <<= (sourceDirectory in Compile)(_ / "java-generated"),
      version in avroConfig := AvroVersion,
      stringType in avroConfig := "String",
      mainClass in (Compile, run) := Some("com.tapad.DataGenerator"),
      mainClass in assembly := Some("com.tapad.DataGenerator"),
      jarName in assembly := s"$name-datagen.jar"
    )
  ).dependsOn(util)
}
