package com.powertrip.config

import enumeratum.{CirisEnum, Enum, EnumEntry}
import eu.timepit.refined.types.net.UserPortNumber

import scala.collection.immutable

sealed trait AppEnvironment extends EnumEntry

object AppEnvironment
    extends Enum[AppEnvironment]
    with CirisEnum[AppEnvironment] {
  case object Development extends AppEnvironment
  case object Production extends AppEnvironment

  val values: immutable.IndexedSeq[AppEnvironment] = findValues
}

final case class Config(port: UserPortNumber)
