name := """shinest_crawler"""

version := "1.0.1.SNAPSHOT"

scalaVersion := "2.11.8"

organization := "shine.st"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.11",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.11" % "test",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "org.scala-lang" % "scala-reflect" % "2.11.8",
  "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.4"
)

libraryDependencies += "shine.st" %% "shinest_common" % "1.0.1.SNAPSHOT"

libraryDependencies += "org.jsoup" % "jsoup" % "1.9.2"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.7"

libraryDependencies += "joda-time" % "joda-time" % "2.9.4"

libraryDependencies += "com.sksamuel.elastic4s" % "elastic4s-core_2.11" % "2.3.0"


// https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient
libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.5.2"
