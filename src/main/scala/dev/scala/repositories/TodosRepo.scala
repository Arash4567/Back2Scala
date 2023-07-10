package dev.scala.repositories

import java.util.UUID

import cats.data.OptionT
import cats.effect.MonadCancelThrow
import cats.effect.Resource
import cats.implicits.catsSyntaxApplicativeErrorId
import cats.implicits.toFunctorOps
import dev.scala.models.Todo
import dev.scala.repositories.sql.TodoSql
import skunk.Session

trait TodosRepo[F[_]] {
  def get: F[List[Todo]]
  def update(id: UUID)(update: Todo => Todo): F[Unit]
  def create(todo: Todo): F[Unit]
  def delete(id: UUID): F[Unit]
}

object TodosRepo {
  def make[F[_]: MonadCancelThrow](session: Resource[F, Session[F]]): TodosRepo[F] =
    new TodosRepo[F] {
      override def get: F[List[Todo]] =
        session.use(_.execute(TodoSql.select))

      override def update(id: UUID)(update: Todo => Todo): F[Unit] =
        OptionT(session.use(_.option(TodoSql.findById)(id)))
          .cataF(
            new Exception(s"Todo not found by id: [$id]").raiseError[F, Unit],
            todo => session.use(_.execute(TodoSql.update)(update(todo))),
          )

      override def create(todo: Todo): F[Unit] = session.use(_.execute(TodoSql.insert)(todo))

      override def delete(id: UUID): F[Unit] = session.use(_.execute(TodoSql.delete)(id))
    }
}
