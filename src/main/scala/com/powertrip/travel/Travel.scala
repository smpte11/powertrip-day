package com.powertrip.travel

import java.time.LocalDateTime
import java.util.UUID

import cats.data.NonEmptyChain

case class Day(
    date: LocalDateTime,
    activities: NonEmptyChain[Int],
    location: Location
)

case class Travel(
    id: Option[UUID],
    name: String,
    destination: String,
    from: LocalDateTime,
    to: LocalDateTime
)
