package com.powertrip.travel

import java.time.LocalDateTime

import cats.data.NonEmptyChain
import cats.effect._
import io.circe.refined._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.rho.RhoRoutes
import eu.timepit.refined.auto._
import org.http4s.HttpRoutes
import org.http4s.server.Router

class Route[F[_]: Sync] extends Http4sDsl[F] {
  // TODO move `api` and `version` to config
  val api = "api"
  val version = "v1"

  val travelService = HttpRoutes.of[F] {
    case GET -> Root / "travels" / id =>
      val activities = NonEmptyChain(2, 2, 3)
      val day = Day(
        date = LocalDateTime.now(),
        activities = activities,
        location = Location(1, Coordinate(43.0f, 71.0f))
      )
      Ok(day)
  }

  val app = Router(
    s"/$api/$version" -> travelService
  )
}
