import sbt._

object Dependencies {

  val sparkDependencies = Seq(
    "org.apache.spark" %% "spark-core" % Versions.Spark,
    "org.apache.spark" %% "spark-sql" % Versions.Spark,
    "org.apache.spark" %% "spark-avro" % Versions.Spark
  )

  val deltaDependencies = Seq(
    "io.delta" %% "delta-core" % Versions.Delta
  )

  val avroDependencies = Seq(
    "org.apache.spark" %% "spark-avro" % Versions.Spark
  )

  val jmhDependencies = Seq(
    "org.openjdk.jmh" % "jmh-core" % Versions.Jmh,
    "org.openjdk.jmh" % "jmh-generator-annprocess" % Versions.Jmh
  )

  val testDependencies = Seq(
    "org.scalatest" %% "scalatest" % Versions.ScalaTest % Test
  )

  val scalaFmtDependencies = Seq(
    "org.scalameta" %% "scalafmt-dynamic" % Versions.Scalafmt
  )
}
