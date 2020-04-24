package com.powertrip.config

import cats.effect._
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import eu.timepit.refined.auto._
import org.flywaydb.core.Flyway

object Database {
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

  def init[F[_]: Async](transactor: HikariTransactor[F]): F[Unit] = {
    val F = implicitly[Async[F]]
    transactor.configure { dataSource =>
      F.pure {
        val flyway = Flyway.configure.dataSource(dataSource).load
        flyway.migrate
        ()
      }
    }
  }
}
