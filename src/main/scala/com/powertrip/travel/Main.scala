package com.powertrip.travel

import cats.Applicative
import cats.effect._
import cats.implicits._
import ciris._
import ciris.refined._
import com.powertrip.config._
import com.powertrip.config.AppEnvironment.Development
import eu.timepit.refined.auto._
import eu.timepit.refined.cats._
import eu.timepit.refined.types.net.UserPortNumber
import eu.timepit.refined.string.Uri
import fs2.Stream
import org.http4s.implicits._
import org.http4s.server.blaze._
import org.http4s.server.middleware.Logger

object Main extends IOApp {

  val apiConfig = for {
    port <- env("PORT").as[UserPortNumber].default(8081)
  } yield ApiConfig(port = port)

  val dbConfig = for {
    password <- env("DB_PASS").as[DatabasePassword].default("test").secret,
  } yield DbConfig(
    driver = "org.postgresql.Driver",
    url = "http://localhost:5432",
    user = "traveller",
    password = password
  )

  val config: ConfigValue[Config] = (
    apiConfig,
    dbConfig
  ).parMapN { (api, database) =>
    Config(api = api, database = database)
  }

  def run(args: List[String]): IO[ExitCode] =
    stream[IO].compile.drain.as(ExitCode.Success)

  def stream[F[_]: ConcurrentEffect: Applicative: ContextShift: Timer]
      : Stream[F, ExitCode] =
    for {
      configuration <- Stream.eval(config.load[F])
      httpApp = new Route[F].routes.toRoutes().orNotFound
      enhanced = Logger.httpApp(logHeaders = true, logBody = true)(httpApp)
      exitCode <- BlazeServerBuilder[F]
        .bindHttp(port = configuration.api.port, host = "0.0.0.0")
        .withHttpApp(enhanced)
        .serve
    } yield exitCode
}
