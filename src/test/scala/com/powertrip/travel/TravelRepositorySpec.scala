package com.powertrip.travel

import cats.effect._
import cats.implicits._
import ciris.refined._
import ciris.Secret
import com.powertrip.config._
import doobie._
import doobie.implicits._
import doobie.postgres.implicits._
import doobie.implicits.javatime._
import doobie.scalatest.IOChecker
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor
import eu.timepit.refined.auto._
import eu.timepit.refined.cats._
import org.scalatest.flatspec.{AnyFlatSpec => FlatSpec}
import org.scalatest.matchers.should.Matchers
import org.scalatest.BeforeAndAfterAll
import java.time.LocalDateTime
import java.{util => ju}

class TravelRepositorySpec
    extends FlatSpec
    with Matchers
    with IOChecker
    with BeforeAndAfterAll {
  implicit val cs = IO.contextShift(ExecutionContexts.synchronous)

  val config: DbConfig = DbConfig(
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost:5432/test_travel_db",
    "test_traveller",
    Secret[DatabasePassword]("test"),
    32
  )

  val transactor = Transactor.fromDriverManager[IO](
    config.driver,
    config.url,
    config.user,
    config.password.value
  )

  val id: ju.UUID = ju.UUID.randomUUID()
  val drop = sql"""drop table if exists travel""".update.run
  val create = sql"""create table travel (
    id uuid,
    name varchar not null,
    destination varchar not null,
    start_at timestamp not null,
    end_at timestamp not null
  )""".update.run
  val seed = sql"""insert into travel (
    id,
    name,
    destination,
    start_at,
    end_at
  ) values (
    $id,
    'test',
    'test',
    ${LocalDateTime.now},
    ${LocalDateTime.now}
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
      Queries.insertTravel("test", "test", LocalDateTime.now, LocalDateTime.now)
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
