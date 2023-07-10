package dev.scala

import cats.effect.{ExitCode, IO, IOApp, Resource}
import com.comcast.ip4s.IpLiteralSyntax
import dev.scala.repositories.TodosRepo
import dev.scala.routes.ExampleRouter
import org.http4s.HttpRoutes
import org.http4s.ember.server.EmberServerBuilder
import org.typelevel.log4cats.SelfAwareStructuredLogger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import skunk.Session
import skunk.util.Typer

object Main extends IOApp {
  implicit val logger: SelfAwareStructuredLogger[IO] =
    Slf4jLogger.getLoggerFromName[IO]("back2scala")

  def skunkSession[F[_]](config: DatabaseConfig) =
    Session.pooledF[F](
      host = config.host,
      port = config.port,
      user = config.user,
      password = Some(config.password),
      database = config.database,
      max = config.poolSize,
      strategy = Typer.Strategy.SearchPath
    )

  def server[F[_]](routes: HttpRoutes[F]) =
    EmberServerBuilder
    .default[IO]
    .withHost(host"localhost")
    .withPort(port"9000")
    .withHttpApp(routes.orNotFound)
    .build
  def program[F[_]] =
    for {
      config <- Resource.Eval(ConfigLoader.load[F, Config])
      session <- skunkSession[F]
      todosRepo = TodosRepo.make(session)
      routes = new ExampleRouter[F](todosRepo).routes
      server <- server[F](routes)
    } yield server
  override def run(args: List[String]): IO[ExitCode] =
    program.useForever
}
