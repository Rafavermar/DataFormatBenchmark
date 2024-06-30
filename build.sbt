name := "DataFormatBenchmark"
version := "1.0"
scalaVersion := "2.13.14"

libraryDependencies ++= Dependencies.sparkDependencies ++
  Dependencies.deltaDependencies ++
  Dependencies.avroDependencies ++
  Dependencies.jmhDependencies ++
  Dependencies.testDependencies ++
  Dependencies.scalaFmtDependencies

javaOptions ++= Seq(
  "-Xmx2G",
  "-Xms2G",
  "-XX:ReservedCodeCacheSize=512M",
  "-XX:+UseG1GC",
  "-XX:MaxGCPauseMillis=200",
  "-XX:G1ReservePercent=15",
  "-XX:InitiatingHeapOccupancyPercent=25",
  "-XX:+UseStringDeduplication",
  "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED"
)

enablePlugins(JmhPlugin)
Jmh / fork := true
Jmh / javaOptions += "-Djmh.ignoreLock=true"
