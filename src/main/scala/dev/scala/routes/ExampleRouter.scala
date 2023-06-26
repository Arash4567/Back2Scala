package dev.scala.routes

import cats.Monad
import cats.implicits.catsSyntaxApply
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import org.typelevel.log4cats.Logger

class ExampleRouter[F[_]: Monad](implicit logger: Logger[F]) extends Http4sDsl[F] {
  private object CountryQueryParamMatcher extends QueryParamDecoderMatcher[String]("country")

  private val publicRouter = HttpRoutes.of[F] {
    case req @ GET -> Root / "status" :? CountryQueryParamMatcher(country) =>
      logger.info(s"Ura! - $country") *>
        Ok(s"Ok - $country")

    case req @ GET -> Root / "ping" =>
      logger.info(s"Ura!") *>
        Ok(s"Ok")

    case req @ GET -> Root / "status" / "start" =>
      logger.info(s"Ura!") *>
        Ok(s"Ok - start")
  }
  val routes: HttpRoutes[F] = Router(
    "api/v1" -> publicRouter
  )
}
