version := "0.0.1"

name := "metrics-scala"

scalaVersion := "2.13.6"

scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-Xlint:_")

libraryDependencies ++= Seq(
  "com.lihaoyi" %% "mainargs" % "0.2.1",
  "com.lihaoyi" %% "os-lib" % "0.7.7",
  "org.typelevel" %% "cats-core" % "2.6.1",
  "org.eclipse.jgit" % "org.eclipse.jgit" % "5.11.1.202105131744-r",
  "com.typesafe.slick" %% "slick" % "3.3.3",
  "org.xerial" % "sqlite-jdbc" % "3.34.0",
  "org.log4s" %% "log4s" % "1.10.0",
  "org.slf4j" % "slf4j-simple" % "1.7.30",
  "org.scalameta" %% "munit" % "0.7.26" % Test
)

Test / parallelExecution := false

enablePlugins(JavaAppPackaging)

maintainer := "laufer@cs.luc.edu"
