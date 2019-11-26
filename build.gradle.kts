val scalaVersion = "2.13.1"
val http4sVersion = "0.21.0-M5"
val rhoVersion = "0.20.0-M1"
val circeVersion = "0.12.3"
val scalatestVersion = "3.1.0-RC3"
val logbackVersion = "1.2.3"
val cirisVersion = "1.0.2"


plugins {
    id("scala")
    id("com.google.cloud.tools.jib") version "1.6.1"
}

repositories {
    mavenCentral()
}

tasks.withType<ScalaCompile> {
    scalaCompileOptions.additionalParameters = listOf(
            "-deprecation",
            "-encoding", "UTF-8",
            "-language:higherKinds",
            "-language:postfixOps",
            "-feature",
            "-Xfatal-warnings"
    )
}

dependencies {
    implementation("org.scala-lang:scala-library:$scalaVersion")

    implementation("is.cir:ciris_2.13:$cirisVersion")
    implementation("is.cir:ciris-enumeratum_2.13:$cirisVersion")
    implementation("is.cir:ciris-refined_2.13:$cirisVersion")

    implementation("io.circe:circe-generic_2.13:$circeVersion")

    implementation("org.http4s:http4s-dsl_2.13:$http4sVersion")
    implementation("org.http4s:http4s-blaze-server_2.13:$http4sVersion")
    implementation("org.http4s:http4s-blaze-client_2.13:$http4sVersion")
    implementation("org.http4s:http4s-circe_2.13:$http4sVersion")

    implementation("org.http4s:rho-core_2.13:$rhoVersion")
    implementation("org.http4s:rho-hal_2.13:$rhoVersion")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    testImplementation("org.scalatest:scalatest_2.13:$scalatestVersion")
    testImplementation("org.scalactic:scalactic_2.13:$scalatestVersion")

    implementation("org.typelevel:kind-projector_2.13.1:0.11.0")
    implementation("com.olegpy:better-monadic-for_2.13.0-RC3:0.3.0")
}
