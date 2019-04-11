import sbt._


object Dependencies {

  val akkaVersion: String = "2.5.22"
  val akkaHttpVersion = "10.0.10"
  val elastic4sVersion = "5.5.4"

  lazy val akka: Seq[ModuleID] = Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion
  )

  lazy val akkaCluster: Seq[ModuleID] = akka ++
    Seq(
      "com.typesafe.akka" %% "akka-cluster" % akkaVersion)


  lazy val akkaHttp = Seq(
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion
  )

  lazy val scalaTest: ModuleID = "org.scalatest" %% "scalatest" % "3.0.1" % "test"

  lazy val orgJsoup: ModuleID = "org.jsoup" % "jsoup" % "1.10.3"

  lazy val logback: ModuleID = "ch.qos.logback" % "logback-classic" % "1.2.3"

  //  lazy val typesafeConfig: ModuleID = "com.typesafe" % "config" % "1.3.1"

  lazy val commonLang: ModuleID = "org.apache.commons" % "commons-lang3" % "3.6"

  //  lazy val guice: ModuleID = "com.google.inject" % "guice" % "4.1.0"

  //  lazy val playJson: ModuleID = "com.typesafe.play" %% "play-json" % "2.6.0"

  //  lazy val mariadb: ModuleID = "org.mariadb.jdbc" % "mariadb-java-client" % "2.0.1"

  //  lazy val jodaTime: ModuleID = "joda-time" % "joda-time" % "2.9.7"


  lazy val elastic4s: Seq[ModuleID] = Seq(
    "com.sksamuel.elastic4s" %% "elastic4s-core" % elastic4sVersion,

    // for the http client
    "com.sksamuel.elastic4s" %% "elastic4s-http" % elastic4sVersion,
    "com.sksamuel.elastic4s" %% "elastic4s-circe" % elastic4sVersion

  )

  lazy val shinestCommon: ModuleID = "shine.st" %% "common" % "2.0.0"

  lazy val httpclient: ModuleID = "org.apache.httpcomponents" % "httpclient" % "4.5.3"

  lazy val crawler: Seq[ModuleID] = Seq(shinestCommon, orgJsoup, logback, scalaTest, httpclient) ++ elastic4s ++ akka ++ akkaHttp
}
