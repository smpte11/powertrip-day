val http4sVersion = "0.20.15"
val rhoVersion = "0.20.0-M1"
val circeVersion = "0.12.3"
val scalatestVersion = "3.1.0-RC3"
val logbackVersion = "1.2.3"
val cirisVersion = "1.0.2"

lazy val root = (project in file("."))
  .settings(
    scalaVersion := "2.12.10",
    name := "powertrip-day",
    organization := "com.powertrip",
    version := "0.0.1-SNAPSHOT",
    libraryDependencies ++= Seq(
      "is.cir" %% "ciris" % cirisVersion,
      "is.cir" %% "ciris-enumeratum" % cirisVersion,
      "is.cir" %% "ciris-refined" % cirisVersion,
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,
      "io.circe" %% "circe-refined" % circeVersion,
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "org.http4s" %% "http4s-blaze-client" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "org.http4s" %% "rho-core" % rhoVersion,
      "ch.qos.logback" % "logback-classic" % logbackVersion,
      "org.scalatest" %% "scalatest" % scalatestVersion % Test,
      "org.scalactic" %% "scalactic" % scalatestVersion % Test
    ),
    addCompilerPlugin(
      "org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full
    ),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.0")
  )

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Ypartial-unification",
  "-Xfatal-warnings"
)

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

mainClass in Compile := Some("com.powertrip.day.Main")
