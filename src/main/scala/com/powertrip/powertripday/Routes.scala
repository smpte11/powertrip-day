package com.powertrip.powertripday

import java.time.LocalDateTime

import cats.data.{Kleisli, NonEmptyList}
import cats.effect.IO
import com.powertrip.powertripday.Days.Day
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits._


object Routes {
  def dayRoutes(): Kleisli[IO, Request[IO], Response[IO]] = HttpRoutes.of[IO] {
    case GET -> Root / "days" / _ =>
      val day = Day(date=LocalDateTime.now(), activities=NonEmptyList.of(1))
      for {
        response <- Ok(day)
      } yield response
  }.orNotFound
}