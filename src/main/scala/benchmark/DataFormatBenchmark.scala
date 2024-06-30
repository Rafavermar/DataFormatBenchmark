package benchmark

import org.apache.spark.sql.{SparkSession, DataFrame, SaveMode, Row}
import org.apache.spark.sql.types.{StructType, StructField, IntegerType, StringType}
import org.openjdk.jmh.annotations._
import org.openjdk.jmh.runner.Runner
import org.openjdk.jmh.runner.options.OptionsBuilder
import java.io.File

@State(Scope.Benchmark)
class DataFormatBenchmark {

  var spark: SparkSession = _
  var data: DataFrame = _

  @Param(Array("small", "large"))
  var dataset: String = _

  @Setup(Level.Trial)
  def setup(): Unit = {
    spark = SparkSession.builder
      .appName("DataFormatBenchmark")
      .master("local[*]")
      .getOrCreate()

    // Load the appropriate dataset based on the parameter
    dataset match {
      case "small" => data = loadSmallDataset()
      case "large" => data = loadLargeDataset()
    }
  }

  def loadSmallDataset(): DataFrame = {
    val schema = StructType(List(
      StructField("id", IntegerType, nullable = false),
      StructField("name", StringType, nullable = false),
      StructField("age", IntegerType, nullable = false)
    ))

    val data = Seq(
      Row(1, "Alice", 29),
      Row(2, "Bob", 31),
      Row(3, "Cathy", 25)
    )

    spark.createDataFrame(spark.sparkContext.parallelize(data), schema)
  }

  def loadLargeDataset(): DataFrame = {
    val schema = StructType(List(
      StructField("id", IntegerType, nullable = false),
      StructField("name", StringType, nullable = false),
      StructField("age", IntegerType, nullable = false)
    ))

    val data = (1 to 10000).map(i => Row(i, s"Name$i", i % 100))

    spark.createDataFrame(spark.sparkContext.parallelize(data), schema)
  }

  @TearDown(Level.Trial)
  def tearDown(): Unit = {
    if (spark != null) {
      spark.stop()
    }
    // Clean up temporary files
    deleteRecursively(new File("/tmp/benchmark_data"))
  }

  def deleteRecursively(file: File): Unit = {
    if (file.isDirectory) {
      file.listFiles.foreach(deleteRecursively)
    }
    if (file.exists) {
      file.delete()
    }
  }

  def cleanDirectory(path: String): Unit = {
    deleteRecursively(new File(path))
    new File(path).mkdirs()
  }

  @Benchmark
  def writeParquet(): Unit = {
    cleanDirectory("/tmp/parquet_data")
    data.write.mode(SaveMode.Overwrite).parquet("/tmp/parquet_data")
  }

  @Benchmark
  def readParquet(): Unit = {
    val parquetCount = spark.read.parquet("/tmp/parquet_data").count()
  }

  @Benchmark
  def writeORC(): Unit = {
    cleanDirectory("/tmp/orc_data")
    data.write.mode(SaveMode.Overwrite).orc("/tmp/orc_data")
  }

  @Benchmark
  def readORC(): Unit = {
    val orcCount = spark.read.orc("/tmp/orc_data").count()
  }

  @Benchmark
  def writeDelta(): Unit = {
    cleanDirectory("/tmp/delta_data")
    data.write.format("delta").mode(SaveMode.Overwrite).save("/tmp/delta_data")
  }

  @Benchmark
  def readDelta(): Unit = {
    val deltaCount = spark.read.format("delta").load("/tmp/delta_data").count()
  }

  @Benchmark
  def writeAvro(): Unit = {
    cleanDirectory("/tmp/avro_data")
    data.write.format("avro").mode(SaveMode.Overwrite).save("/tmp/avro_data")
  }

  @Benchmark
  def readAvro(): Unit = {
    val avroCount = spark.read.format("avro").load("/tmp/avro_data").count()
  }
}

object DataFormatBenchmarkRunner {
  def main(args: Array[String]): Unit = {
    val opt = new OptionsBuilder()
      .include(classOf[DataFormatBenchmark].getSimpleName)
      .warmupIterations(1)
      .measurementIterations(1)
      .forks(1)
      .threads(1)
      //.param("dataset", if (args.isEmpty) "small" else args(0))
      .build()

    new Runner(opt).run()
  }
}
