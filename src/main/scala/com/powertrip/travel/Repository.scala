package com.powertrip.travel

import doobie.implicits._
import doobie.postgres.implicits._
import doobie.util.transactor.Transactor
import java.{util => ju}

import cats.effect.Bracket

class Repository[F[_]: Bracket[*[_], Throwable]](xa: Transactor[F]) {
  case object TravelNotFound extends Throwable

  val queries: Queries.type = Queries

  def fetchAll: fs2.Stream[F, Travel] = queries.allTravels.stream.transact(xa)

  def fetchById(id: ju.UUID): F[Option[Travel]] =
    queries.byId(id).option.transact(xa)

  def create(travel: Travel): F[Travel] =
    queries
      .insertTravel(
        name = travel.name,
        destination = travel.destination,
        startAt = travel.start,
        endAt = travel.end
      )
      .withUniqueGeneratedKeys[ju.UUID]("id")
      .map(id => travel.copy(id = Some(id)))
      .transact(xa)

}
