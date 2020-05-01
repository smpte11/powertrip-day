package com.powertrip.travel

import java.time.OffsetDateTime
import java.util.UUID

import cats.data.NonEmptyChain

case class Day(
    date: OffsetDateTime,
    activities: NonEmptyChain[Int],
    location: Location
)

case class Travel(
    id: Option[UUID],
    name: String,
    destination: String,
    start: OffsetDateTime,
    end: OffsetDateTime
)
