package com.powertrip.powertripday

import io.circe.generic.semiauto._
import org.http4s.circe._
import java.time.LocalDateTime

import cats.data.NonEmptyList
import cats.effect.IO
import io.circe.Encoder
import org.http4s.EntityEncoder

trait Days {}

object Days {
  def apply(implicit  ev: Days): Days = ev

  case class Day(date: LocalDateTime, activities: NonEmptyList[Int])

  object Day {
    implicit val dayEncoder: Encoder[Day] = deriveEncoder[Day]
    implicit def dayEntityEncoder: EntityEncoder[IO, Day] = jsonEncoderOf[IO, Day]
  }
}
