package com.powertrip.config

import cats.effect.Blocker
import cats.effect.IO
import doobie.hikari.HikariTransactor
import eu.timepit.refined.auto._
import scala.concurrent.ExecutionContext
import cats.effect.ContextShift

object Database {
  def transactor(
      config: DbConfig,
      executionContext: ExecutionContext,
      blocker: Blocker
  )(implicit contextShift: ContextShift[IO]) = {
    HikariTransactor.newHikariTransactor[IO](
      config.driver,
      config.url,
      config.user,
      config.password.value,
      executionContext,
      blocker
    )
  }

}
