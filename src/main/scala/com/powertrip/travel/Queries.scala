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

import java.time.OffsetDateTime

object Queries {
  private val BaseSelect =
    fr"select id, name, destination, start_at, end_at from travel"

  val allTravels: doobie.Query0[Travel] =
    BaseSelect
      .query[Travel]

  def byId(id: ju.UUID): doobie.Query0[Travel] = (BaseSelect ++ fr"where id = $id").query[Travel]

  def insertTravel(
      name: String,
      destination: String,
      startAt: OffsetDateTime,
      endAt: OffsetDateTime
  ): doobie.Update0 = sql"""insert into travel (
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
