package com.powertrip

import cats.Applicative
import eu.timepit.refined._
import eu.timepit.refined.api._
import eu.timepit.refined.numeric._
import io.circe._
import io.circe.generic.semiauto._
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

package object day {
  // Refined primitive types
  type Lat = Float Refined Interval.Closed[W.`-90.0f`.T, W.`90.0f`.T]
  type Long = Float Refined Interval.Closed[W.`-180.0f`.T, W.`180.0f`.T]

  // Domain value classes
  case class Coordinate(lat: Int, long: Int)
  // case class Location(id: Int Refined Positive, coordinates: Coordinate)

  // Encoders and decoders
  implicit val coordinateEncoder: Encoder[Coordinate] =
    deriveEncoder[Coordinate]
  // implicit val locationEncoder: Encoder[Location] = deriveEncoder

  implicit val dayEncoder: Encoder[Day] = deriveEncoder[Day]
  implicit def dayEntityEncoder[F[_]: Applicative]: EntityEncoder[F, Day] =
    jsonEncoderOf[F, Day]

}
