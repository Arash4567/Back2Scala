package dev.scala.routes

import cats.Monad
import cats.implicits.catsSyntaxApply
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import org.typelevel.log4cats.Logger

class ExampleRouter[F[_]: Monad](implicit logger: Logger[F]) extends Http4sDsl[F] {
  private val publicRouter = HttpRoutes.of[F] {
    case req @ GET -> Root / "status" :? aa =>
      logger.info("Ura!") *>
        Ok("Ok")
  }
  val routes: HttpRoutes[F] = Router(
    "api/v1" -> publicRouter
  )
}
