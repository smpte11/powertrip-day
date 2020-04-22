package com.powertrip.config

import cats.effect.Async
import doobie.hikari.HikariTransactor
import org.flywaydb.core.Flyway

object Database {
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
