package dev.scala.models

import java.util.UUID

import dev.scala.enums.Status

case class Todo(
    id: UUID,
    title: String,
    description: String,
    status: Status,
  )
