import sbt._
import sbt.Keys._
import sbtassembly.AssemblyKeys._
import sbtassembly.{MergeStrategy, PathList}

object Settings {
  lazy val commonSettings = Seq(
    organization := "shine.st",
    version := "0.0.1",
    scalaVersion := "2.12.8",
    parallelExecution in test := false,
    compileOrder in Compile := CompileOrder.Mixed, // change to Mixed for Play
    fork := true,
    resolvers += "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository"
  )


  lazy val assemblySettings = Seq(
    assemblyMergeStrategy in assembly := {
      case PathList("org", "joda", "time", "base", "BaseDateTime.class") => MergeStrategy.first
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    },
    test in assembly := {}
  )

}
