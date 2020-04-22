package com.powertrip.travel

import cats.effect._
import cats.implicits._
import ciris._
import ciris.refined._
import com.powertrip.config._
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import eu.timepit.refined.auto._
import eu.timepit.refined.cats._
import eu.timepit.refined.types.net.UserPortNumber
import org.http4s.implicits._
import org.http4s.server.Server
import org.http4s.server.blaze._
import org.http4s.server.middleware.Logger

object Main extends IOApp {

  val apiConfig: ConfigValue[ApiConfig] = for {
    port <- env("PORT").as[UserPortNumber].default(8081)
  } yield ApiConfig(port = port)

  val dbConfig: ConfigValue[DbConfig] = for {
    password <- env("DB_PASS").as[DatabasePassword].default("test").secret
    poolSize <- env("THREAD_POOL_SIZE").as[Int].default(32)
  } yield DbConfig(
    driver = "org.postgresql.Driver",
    url = "jdbc:postgresql://powertrip-travel-postgresql/travel-db",
    user = "traveller",
    password = password,
    threadPoolSize = poolSize
  )

  val config: ConfigValue[Config] =
    env("ENV").as[AppEnvironment].default(AppEnvironment.Development).flatMap {
      _ =>
        (
          apiConfig,
          dbConfig
        ).parMapN { (api, database) =>
          Config(api = api, database = database)
        }
    }

  def transactor[F[_]: Async: ContextShift](
      conf: DbConfig
  ): Resource[F, HikariTransactor[F]] =
    for {
      ce <- ExecutionContexts.fixedThreadPool[F](conf.threadPoolSize)
      blocker <- Blocker[F]
      transactor <- HikariTransactor.newHikariTransactor(
        conf.driver,
        conf.url,
        conf.user,
        conf.password.value,
        ce,
        blocker
      )
    } yield transactor

  def server[F[_]: ConcurrentEffect: ContextShift: Timer](
      conf: Config
  ): Resource[F, Server[F]] =
    for {
      xa <- transactor(conf.database)
      httpApp = new Route[F].routes.toRoutes().orNotFound
      enhanced = Logger.httpApp(logHeaders = true, logBody = true)(httpApp)
      server <- BlazeServerBuilder[F]
        .bindHttp(port = conf.api.port, host = "0.0.0.0")
        .withHttpApp(enhanced)
        .resource
    } yield server

  def run(args: List[String]): IO[ExitCode] =
    config
      .load[IO]
      .flatMap(conf => server[IO](conf).use(_ => IO.never.as(ExitCode.Success)))
}
