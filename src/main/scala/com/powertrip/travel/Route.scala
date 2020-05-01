package com.powertrip.travel

import cats.effect._
import cats.implicits._
import fs2.Stream
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.{HttpRoutes, MediaType}
import org.http4s.circe._
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.`Content-Type`
import org.http4s.server.Router

class Route[F[_]: Sync](repository: Repository[F]) extends Http4sDsl[F] {
  // TODO move `api` and `version` to config
  val api = "api"
  val version = "v1"
  val resource = "travels"
  val contentType: `Content-Type` = `Content-Type`(MediaType.application.json)

  val travelService: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / resource =>
      Ok(
        Stream("[") ++ repository.fetchAll
          .map(_.asJson.noSpaces)
          .intersperse(",") ++ Stream("]"),
        contentType
      )

    case GET -> Root / resource / UUIDVar(id) =>
      repository.fetchById(id).flatMap {
        _.fold(NotFound())(Ok(_, contentType))
      }

    case req @ POST -> Root / resource =>
      for {
        travel <- req.as[Travel]
        created <- repository.create(travel)
        response <- Created(created.asJson, contentType)
      } yield response

  }

  val app: HttpRoutes[F] = Router(
    s"/$api/$version" -> travelService
  )
}
