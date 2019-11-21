package com.powertrip.day

import cats.effect._
import cats.implicits._
import fs2.{INothing, Stream}
import org.http4s.implicits._
import org.http4s.server.blaze._
import org.http4s.server.middleware.Logger
import cats.Applicative
import org.http4s.server.Router

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    stream[IO].compile.drain.as(ExitCode.Success)

  def stream[F[_]: ConcurrentEffect: Applicative: ContextShift: Timer]: Stream[F, INothing] = {
    val httpApp = Router(
      "/v1" -> new Routes[F].routes
    ).orNotFound
    val enhanced = Logger.httpApp(logHeaders = true, logBody = true)(httpApp)
    for {
      exitCode <- BlazeServerBuilder[F]
        .bindHttp(8081, "0.0.0.0")
        .withHttpApp(enhanced)
        .serve
    } yield exitCode
  }.drain
}
