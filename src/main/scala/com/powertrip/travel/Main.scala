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
    url = "jdbc:postgresql://powertrip-travel-database/travel_db",
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

  def resources[F[_]: ConcurrentEffect: ContextShift](
      conf: Config
  ): Resource[F, (Config, HikariTransactor[F])] =
    for {
      xa <- Database.transactor(conf.database)
    } yield (conf, xa)

  def server[F[_]: ConcurrentEffect: ContextShift: Timer](
      resources: (Config, HikariTransactor[F])
  ): F[ExitCode] = {
    val (conf, xa) = resources
    for {
      _ <- Database.init(xa)
      httpApp = new Route[F](new Repository[F](xa)).app.orNotFound
      enhanced = Logger.httpApp(logHeaders = true, logBody = true)(httpApp)
      exitCode <- BlazeServerBuilder[F]
        .bindHttp(port = conf.api.port, host = "0.0.0.0")
        .withHttpApp(enhanced)
        .serve
        .compile
        .lastOrError
    } yield exitCode
  }

  def run(args: List[String]): IO[ExitCode] =
    config
      .load[IO]
      .flatMap(resources[IO](_).use(server[IO]))
}
