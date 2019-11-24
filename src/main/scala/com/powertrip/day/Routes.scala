package com.powertrip.day

import java.time.LocalDateTime

import cats.data.NonEmptyList
import cats.effect._
import com.powertrip.day.Day._
import org.http4s.dsl.Http4sDsl
import org.http4s.rho.RhoRoutes

class Routes[F[_]: Sync] extends Http4sDsl[F] {
  // TODO move `api` and `version` to config
  val api = "api"
  val version = "v1"
  val resource = "days"

  val routes: RhoRoutes[F] = new RhoRoutes[F] {
    GET / api / version / resource / pathVar[Int] |>> { _: Int =>
      val day = Day(LocalDateTime.now(), NonEmptyList.of(1))
      Ok(day)
    }
  }
}
