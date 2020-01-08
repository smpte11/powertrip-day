package com.powertrip.day

import java.time.LocalDateTime

import cats.data.NonEmptyChain

case class Day(date: LocalDateTime, activities: NonEmptyChain[Int])
