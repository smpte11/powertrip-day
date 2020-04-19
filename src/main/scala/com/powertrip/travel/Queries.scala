package com.powertrip.travel

import cats._
import cats.data._
import cats.implicits._
import doobie._
import doobie.implicits._
import doobie.postgres._
import doobie.postgres.implicits._
import doobie.implicits.javatime._
import java.{util => ju}
import cats.effect.IO
import java.time.LocalDateTime

object Queries {
  val allTravels =
    sql"select id, name, destination, start, 'end' from travel"
      .query[Travel]

  def insertTravel(
      name: String,
      destination: String,
      startAt: LocalDateTime,
      endAt: LocalDateTime
  ) = sql"""insert into travel (
    name,
    destination,
    start_at,
    end_at
  ) values (
    $name,
    $destination,
    $startAt,
    $endAt
  )""".update
}
