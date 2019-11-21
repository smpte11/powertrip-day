package com.powertrip.day

import cats.effect._
import cats.implicits._
import fs2.Stream
import org.http4s.server.blaze._
import org.http4s.server.middleware.Logger
import cats.Applicative

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    stream[IO].compile.drain.as(ExitCode.Success)

  def stream[F[_]: ConcurrentEffect: Applicative: ContextShift: Timer] = {
    val httpApp = Routes.dayRoutes[F]()
    val enhanced = Logger.httpApp(true, true)(httpApp)
    for {
      exitCode <- BlazeServerBuilder[F]
        .bindHttp(8081, "0.0.0.0")
        .withHttpApp(enhanced)
        .serve
    } yield exitCode
  }.drain
}
