package dev.scala.repositories.sql

import java.util.UUID

import dev.scala.enums.Status
import dev.scala.models.Todo
import skunk._
import skunk.codec.all.`enum`
import skunk.codec.all.uuid
import skunk.codec.all.varchar
import skunk.data.Type
import skunk.implicits._
object TodoSql {
  private val statusCodec = `enum`[Status](Status, Type("status"))
  private val columns = uuid *: varchar *: varchar *: statusCodec
  private val codec: Codec[Todo] = columns.to[Todo]

  val select: Query[Void, Todo] =
    sql"""select * from todos""".query(codec)

  val update: Command[Todo] =
    sql"""update todos set 
         title = $varchar,
         description = $varchar,
         status = $statusCodec 
         where id = $uuid
         """
      .command
      .contramap {
        case t: Todo =>
          t.title *: t.description *: t.status *: t.id *: EmptyTuple
      }

  val insert: Command[Todo] =
    sql"""insert into todos(title, description, status)
          values ($codec)""".command

  val delete: Command[UUID] =
    sql"""delete form todos where id = $uuid""".command

  val findById: Query[UUID, Todo] =
    sql"""select * form todos where id = $uuid""".query(codec)
}
