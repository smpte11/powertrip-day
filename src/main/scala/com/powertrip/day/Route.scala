package com.powertrip.day

import java.time.LocalDateTime

import cats.data.NonEmptyChain
import cats.effect._
import org.http4s.dsl.Http4sDsl
import org.http4s.rho.RhoRoutes

class Route[F[_]: Sync] extends Http4sDsl[F] {
  // TODO move `api` and `version` to config
  val api = "api"
  val version = "v1"
  val resource = "days"

  val routes: RhoRoutes[F] = new RhoRoutes[F] {
    GET / api / version / resource / pathVar[Int] |>> { _: Int =>
      val activities = NonEmptyChain(2, 2, 3)
      val day = Day(date = LocalDateTime.now(), activities = activities)
      Ok(day)
    }
  }
}
