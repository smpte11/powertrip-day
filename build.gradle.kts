val scalaVersion = "2.12.10"
val http4sVersion = "0.20.8"
val rhoVersion = "0.20.0-M1"
val circeVersion = "0.12.3"
val specs2Version = "4.8.0"
val logbackVersion = "1.2.3"


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
            "-Ypartial-unification",
            "-Xfatal-warnings"
    )
}

dependencies {
    implementation("org.scala-lang:scala-library:$scalaVersion")

    implementation("io.circe:circe-generic_2.12:$circeVersion")

    implementation("org.http4s:http4s-dsl_2.12:$http4sVersion")
    implementation("org.http4s:http4s-blaze-server_2.12:$http4sVersion")
    implementation("org.http4s:http4s-blaze-client_2.12:$http4sVersion")
    implementation("org.http4s:http4s-circe_2.12:$http4sVersion")

    implementation("org.http4s:rho-core_2.12:$rhoVersion")
    implementation("org.http4s:rho-hal_2.12:$rhoVersion")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    implementation("org.typelevel:kind-projector_2.12.10:0.11.0")
    implementation("com.olegpy:better-monadic-for_2.12:0.3.1")

    testImplementation("org.specs2:specs2-core_2.12:$specs2Version")

}
