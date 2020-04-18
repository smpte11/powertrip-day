package com.powertrip.travel

import cats.implicits._
import ciris.refined._
import com.powertrip.config._
import org.scalatest.flatspec.{AnyFlatSpec => FlatSpec}
import eu.timepit.refined.auto._
import eu.timepit.refined.cats._
import ciris.Secret

class TravelRepositorySpec extends FlatSpec {

  val config: DbConfig = DbConfig(
    "org.h2.Driver",
    "jdbc:h2:mem:test-travel-db;MODE=PostgreSQL",
    "test-traveller",
    Secret[DatabasePassword]("test")
  )

  "Travel repository" should "save a new instance" in {}
}
