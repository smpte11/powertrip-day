package com.powertrip.day

import io.circe.generic.semiauto._
import org.http4s.circe._
import java.time.LocalDateTime

import cats.data.NonEmptyList
import cats.effect.IO
import io.circe.Encoder
import org.http4s.EntityEncoder
import cats.Applicative

case class Day(date: LocalDateTime, activities: NonEmptyList[Int])

object Day {
  implicit val dayEncoder: Encoder[Day] = deriveEncoder[Day]
  implicit def dayEntityEncoder[F[_]: Applicative]: EntityEncoder[F, Day] =
    jsonEncoderOf[F, Day]
}
