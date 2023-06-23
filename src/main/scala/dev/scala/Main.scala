package dev.scala

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import com.comcast.ip4s.IpLiteralSyntax
import dev.scala.routes.ExampleRouter
import org.http4s.HttpRoutes
import org.http4s.ember.server.EmberServerBuilder
import org.typelevel.log4cats.SelfAwareStructuredLogger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Main extends IOApp {
  implicit val logger: SelfAwareStructuredLogger[IO] =
    Slf4jLogger.getLoggerFromName[IO]("back2scala")
  val routes: HttpRoutes[IO] = new ExampleRouter[IO].routes

  override def run(args: List[String]): IO[ExitCode] =
    EmberServerBuilder
      .default[IO]
      .withHost(host"localhost")
      .withPort(port"9000")
      .withHttpApp(routes.orNotFound)
      .build
      .useForever
}
