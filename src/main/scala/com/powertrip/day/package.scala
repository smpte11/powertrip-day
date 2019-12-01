package com.powertrip

import cats.Applicative
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

package object day {
  implicit val dayEncoder: Encoder[Day] = deriveEncoder[Day]
  implicit def dayEntityEncoder[F[_]: Applicative]: EntityEncoder[F, Day] =
    jsonEncoderOf[F, Day]
}
