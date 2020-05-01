package com.powertrip

import eu.timepit.refined._
import eu.timepit.refined.api._
import eu.timepit.refined.numeric._

package object travel {
  // Refined primitive types
  type Lat = Float Refined Interval.Closed[W.`-90.0f`.T, W.`90.0f`.T]
  type Long = Float Refined Interval.Closed[W.`-180.0f`.T, W.`180.0f`.T]

  // Domain value classes
  case class Coordinate(lat: Lat, long: Long)
  case class Location(id: Int Refined Positive, coordinates: Coordinate)

  // Encoders and decoders

  // Errors
  case object TravelNotFoundError
}
