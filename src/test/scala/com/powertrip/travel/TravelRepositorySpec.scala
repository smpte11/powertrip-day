package com.powertrip.travel

import cats.effect._
import cats.implicits._
import ciris.refined._
import ciris.Secret
import com.powertrip.config._
import doobie._
import doobie.implicits._
import doobie.scalatest.IOChecker
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor
import eu.timepit.refined.auto._
import eu.timepit.refined.cats._
import org.scalatest.flatspec.{AnyFlatSpec => FlatSpec}
import org.scalatest.matchers.should.Matchers
import org.scalatest.BeforeAndAfterAll
import java.time.LocalDateTime

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
    Secret[DatabasePassword]("test")
  )

  val transactor = Transactor.fromDriverManager[IO](
    config.driver,
    config.url,
    config.user,
    config.password.value
  )

  val drop = sql"""drop table if exists travel""".update.run
  val create = sql"""create table travel (
    id uuid,
    name varchar,
    destination varchar,
    start_at timestamp,
    end_at timestamp
  )""".update.run

  override def beforeAll(): Unit = {
    (drop, create).mapN(_ + _).transact(transactor).unsafeRunSync
  }

  "Travel repository" should "insert a new travel" in {
    check(Queries.insertTravel("", "", LocalDateTime.now, LocalDateTime.now))
  }

  "Travel repository" should "get a new travel" in {
    check(Queries.allTravels)
  }
}
