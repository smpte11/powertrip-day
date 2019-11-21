package com.powertrip.day

import java.time.LocalDateTime

import cats.data.NonEmptyList
import cats.effect._
import com.powertrip.day.Day._
import org.http4s._
import org.http4s.dsl.Http4sDsl


class Routes[F[_]: Sync] extends Http4sDsl[F] {
  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "days" / _ =>
      val day = Day(LocalDateTime.now(), NonEmptyList.of(1, 2, 3))
      Ok(day)
  }
}
