val circeVersion = "0.12.3"
val cirisVersion = "1.0.4"
val doobieVersion = "0.8.8"
val http4sVersion = "0.21.1"
val logbackVersion = "1.2.3"
val refinedVersion = "0.9.13"
val rhoVersion = "0.20.0"
val scalatestVersion = "3.1.1"

lazy val root = (project in file("."))
  .settings(
    scalaVersion := "2.13.1",
    name := "powertrip-travel",
    organization := "com.powertrip",
    version := "0.0.1-SNAPSHOT",
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % logbackVersion,
      "eu.timepit" %% "refined-cats" % refinedVersion,
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,
      "io.circe" %% "circe-refined" % circeVersion,
      "is.cir" %% "ciris-enumeratum" % cirisVersion,
      "is.cir" %% "ciris-refined" % cirisVersion,
      "is.cir" %% "ciris" % cirisVersion,
      "org.http4s" %% "http4s-blaze-client" % http4sVersion,
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "rho-core" % rhoVersion,
      "org.tpolecat" %% "doobie-core" % doobieVersion,
      "org.tpolecat" %% "doobie-hikari" % doobieVersion,
      "org.tpolecat" %% "doobie-postgres" % doobieVersion,
      "org.tpolecat" %% "doobie-scalatest" % doobieVersion % Test,
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
  "-Xfatal-warnings"
)

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

mainClass in Compile := Some("com.powertrip.travel.Main")
