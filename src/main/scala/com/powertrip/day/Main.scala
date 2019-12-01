package com.powertrip.day

import cats.Applicative
import cats.effect._
import cats.implicits._
import ciris._
import ciris.refined._
import com.powertrip.config.AppEnvironment.Development
import com.powertrip.config.{AppEnvironment, Config}
import eu.timepit.refined.auto._
import eu.timepit.refined.types.net.UserPortNumber
import fs2.Stream
import org.http4s.implicits._
import org.http4s.server.blaze._
import org.http4s.server.middleware.Logger

object Main extends IOApp {

  val config: ConfigValue[Config] = for {
    _ <- env("ENV").as[AppEnvironment].default(Development)
    port <- env("PORT").as[UserPortNumber].default(8081)
  } yield Config(port = port)

  def run(args: List[String]): IO[ExitCode] =
    stream[IO].compile.drain.as(ExitCode.Success)

  def stream[F[_]: ConcurrentEffect: Applicative: ContextShift: Timer]: Stream[F, ExitCode] = for {
    configuration <- Stream.eval(config.load[F])
    httpApp = new Routes[F].routes.toRoutes().orNotFound
    enhanced = Logger.httpApp(logHeaders = true, logBody = true)(httpApp)
    exitCode <- BlazeServerBuilder[F]
        .bindHttp(port = configuration.port, host = "0.0.0.0")
        .withHttpApp(enhanced)
        .serve
  } yield exitCode
}
