import sbt.Keys.scalacOptions

lazy val ChessScala = (project in file("lib/ChessScala"))
  .settings(
    name := "ChessScala",
    scalaVersion := "3.2.1",
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.10",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % "test",
    libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0",
    libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "2.1.0",
    libraryDependencies += "com.typesafe.play" %% "play-json" % "2.10.0-RC7",
    libraryDependencies += "com.google.inject" % "guice" % "5.1.0",
    libraryDependencies += ("net.codingwell" %% "scala-guice" % "5.1.0").cross(CrossVersion.for3Use2_13),
    libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3",
    libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4",
    libraryDependencies += ("com.softwaremill.sttp.client3" %% "core" % "3.8.13").cross(CrossVersion.for3Use2_13),
    assemblyJarName in assembly := "ChessScala-assembly.jar",
    assemblyMergeStrategy in assembly := {
      case PathList("META-INF", _*) => MergeStrategy.discard
      case _                        => MergeStrategy.first
    }
  )

lazy val WaChessScala = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := "WA_ChessScala",
    scalaVersion := "2.13.11",
    libraryDependencies += guice,
    libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test,
    libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "4.10.0",
    unmanagedJars in Compile += baseDirectory.value / "lib/ChessScala/target/scala-3.2.1/ChessScala-assembly.jar",
    scalacOptions += "-Ytasty-reader",
    (compile in Compile) := ((compile in Compile) dependsOn (assembly in ChessScala)).value
  )



version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .aggregate(ChessScala, WaChessScala)
// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
