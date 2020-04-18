package com.powertrip.travel

import java.time.LocalDateTime

import cats.data.NonEmptyChain

case class Day(
    date: LocalDateTime,
    activities: NonEmptyChain[Int],
    location: Location
)

case class Travel(
    to: LocalDateTime,
    from: LocalDateTime,
    days: NonEmptyChain[Int]
)