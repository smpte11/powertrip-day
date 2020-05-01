package com.powertrip.travel

import java.time.OffsetDateTime
import java.{util => ju}

import cats._
import cats.data._
import cats.implicits._
import cats.effect._
import ciris.Secret
import com.powertrip.config._
import doobie._
import doobie.implicits._
import doobie.implicits.javatime._
import doobie.postgres._
import doobie.postgres.implicits._
import doobie.scalatest.IOChecker
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor
import eu.timepit.refined.auto._
import eu.timepit.refined.cats._
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.{AnyFlatSpec => FlatSpec}
import org.scalatest.matchers.should.Matchers

class TravelRepositorySpec
    extends FlatSpec
    with Matchers
    with IOChecker
    with BeforeAndAfterAll {
  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContexts.synchronous)

  val config: DbConfig = DbConfig(
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost:5432/test_travel_db",
    "test_traveller",
    Secret[DatabasePassword]("test"),
    32
  )

  val transactor: doobie.Transactor[IO] = Transactor.fromDriverManager[IO](
    config.driver,
    config.url,
    config.user,
    config.password.value
  )

  val id: ju.UUID = ju.UUID.randomUUID()
  val drop: doobie.ConnectionIO[Int] = sql"""drop table if exists travel""".update.run
  val create: doobie.ConnectionIO[Int] = sql"""create table travel (
    id uuid,
    name varchar not null,
    destination varchar not null,
    start_at timestamp not null,
    end_at timestamp not null
  )""".update.run
  val seed: doobie.ConnectionIO[Int] = sql"""insert into travel (
    id,
    name,
    destination,
    start_at,
    end_at
  ) values (
    $id,
    'test',
    'test',
    ${OffsetDateTime.now},
    ${OffsetDateTime.now}
  )""".update.run

  override def beforeAll(): Unit = {
    (drop, create, seed)
      .mapN(_ + _ + _)
      .flatMap(_ => seed)
      .transact(transactor)
      .unsafeRunSync
  }

  "Travel repository" should "insert a new travel" in {
    check(
      Queries.insertTravel("test", "test", OffsetDateTime.now, OffsetDateTime.now)
    )
  }

  it should "get a new travel" in {
    check(Queries.allTravels)
  }

  it should "get a single travel by id" in {
    check(
      Queries.byId(id = id)
    )
  }
}
