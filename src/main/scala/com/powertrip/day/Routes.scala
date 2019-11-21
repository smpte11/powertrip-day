package com.powertrip.day

import java.time.LocalDateTime

import cats.data.{Kleisli, NonEmptyList}
import cats.effect.IO
import cats.effect.Sync
import com.powertrip.day.Day._
import io.circe.syntax._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.dsl.Http4sDsl

object Routes {
  def dayRoutes[F[_]: Sync]() = {
    val dsl = new Http4sDsl[F] {}
    import dsl._

    HttpRoutes
      .of[F] {
        case GET -> Root / "days" / _ =>
          val day = Day(LocalDateTime.now(), NonEmptyList.of(1, 2))
          Ok(day)
      }
      .orNotFound
  }

}
