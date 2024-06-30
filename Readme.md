# Data Format Benchmark

## Overview

This project aims to benchmark various data formats (Parquet, ORC, Avro, Delta) using Apache Spark. The goal is to compare the read and write performance of these formats under similar conditions.

## Setup

### Prerequisites

- Java 17
- Scala 2.13.14
- Apache Spark 3.3.1
- sbt 1.10.0

### Installation

1. **Clone the repository:**
   ```sh
   git clone https://github.com/yourusername/data-format-benchmark.git
   cd data-format-benchmark

2. **Build the project:**
   ```sh
   sbt clean compile
   
3. **Run the benchmarks**
   ```sh
   sbt jmh:run -i 10 -wi 5 -f1 -t1
   
To get the results in a json file and terminal output in a log file :
   ```
sbt jmh:run -i 1 -wi 1 -f1 -t1 -p dataset=small -rf json -rff src\main\scala\benchmark\results\benchmark_results.json 2>&1 | tee output.log

   ```
To run specific benchmarks, use the following command:
   ```
   sbt "jmh:run -i 1 -wi 1 -f1 -t1 -p dataset=small -rf json -rff /path/to/benchmark_results.json benchmark.DataFormatBenchmark.writeParquet benchmark.DataFormatBenchmark.readParquet"
   ```
  
## Benchmarks
The benchmarks measure the performance of read and write operations for different data formats. The results are recorded in milliseconds per operation.

### Results Summary
Format	Write Time (ms/op)	Read Time (ms/op)
Parquet	x.xxx	x.xxx
ORC	x.xxx	x.xxx
Avro	x.xxx	x.xxx
Delta	x.xxx	x.xxx

### Example results:
      ```
      [
      {
         "benchmark": "benchmark.DataFormatBenchmark.writeParquet",
         "mode": "avgt",
         "threads": 1,
         "forks": 1,
         "warmupIterations": 5,
         "measurementIterations": 10,
         "primaryMetric": {
         "score": 12.345,
         "scoreError": 0.123,
         "scoreUnit": "ms/op"
      }
    },
      {
      "benchmark": "benchmark.DataFormatBenchmark.readParquet",
      "mode": "avgtS",
      "threads": 1,
      "forks": 1,
    "warmupIterations": 5,
    "measurementIterations": 10,
    "primaryMetric": {
      "score": 6.789,
      "scoreError": 0.067,
      "scoreUnit": "ms/op"
    }
      }
      ] 

## Code Overview

The benchmarks are implemented in DataFormatBenchmark.scala using JMH (Java Microbenchmark Harness) and Spark. The following operations are benchmarked:

Writing data to Parquet, ORC, Avro, and Delta formats.
Reading data from Parquet, ORC, Avro, and Delta formats.

### File Structure
      ```
         data-format-benchmark/
         ├── src/
         │   ├── main/
         │   │   ├── scala/
         │   │   │   └── benchmark/
         │   │   │       └── DataFormatBenchmark.scala
         │   │   │       └── results
         │   │   │            └── benchmark_results.json
         │   └── test/
         │       └── scala/
         │           └── benchmark/
         ├── project/
         │   └── build.properties
         ├── log4j2.properties
         ├── build.sbt
         └── README.md
         
### Setting Dataset Size
In the DataFormatBenchmarkRunner, you can specify the dataset size (small or large) using the param method.

      ```
      object DataFormatBenchmarkRunner {
      def main(args: Array[String]): Unit = {
      val opt = new OptionsBuilder()
      .include(classOf[DataFormatBenchmark].getSimpleName)
      .warmupIterations(1)
      .measurementIterations(1)
      .forks(1)
      .threads(1)
      .param("dataset", "small") // Specify "small" or "large"
      .build()
      
          new Runner(opt).run()
      }
      }

      ```
### Example Code Snippet
The dataset size can be adjusted in the setup method of DataFormatBenchmark:
   ```
      // Load the appropriate dataset based on the parameter
      dataset match {
      case "small" => data = loadSmallDataset()
      // case "large" => data = loadLargeDataset()
      }

   ```

## Dependencies
Dependencies are managed in build.sbt:

      ```
        libraryDependencies ++= Seq(
        "org.apache.spark" %% "spark-sql" % "3.3.1",
        "org.apache.spark" %% "spark-avro" % "3.3.1",
        "io.delta" %% "delta-core" % "1.2.1",
        "org.openjdk.jmh" % "jmh-core" % "1.37",
        "org.openjdk.jmh" % "jmh-generator-annprocess" % "1.37"
        )
        
        enablePlugins(JmhPlugin)
     
     

## Contributing
Contributions are welcome! Please open an issue or submit a pull request.