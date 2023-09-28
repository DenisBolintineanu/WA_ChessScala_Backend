import scala.sys.process._

name := """WA_ChessScala"""
organization := "com.example"

javaHome := Some(file("/Users/denisbolintineanu/Library/Java/JavaVirtualMachines/corretto-11.0.19/Contents/Home"))

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.11"

val preBuildTask = taskKey[Unit]("Task to be executed before build")

preBuildTask := {
  println("Executing pre-build task...")
  Process("sbt assembly", new File("lib/ChessScala")).!
}

compile in Compile := (compile in Compile).dependsOn(preBuildTask).value

unmanagedJars in Compile += baseDirectory.value / "lib/ChessScala/target/scala-3.2.1/ChessScala-assembly-0.1.0-SNAPSHOT.jar"

libraryDependencies ++= Seq(
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test,
  "org.mongodb.scala" %% "mongo-scala-driver" % "4.10.0"
)

scalacOptions += "-Ytasty-reader"
// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
