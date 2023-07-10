package dev.scala.enums

import enumeratum.EnumEntry.Snakecase
import enumeratum._

sealed trait Status extends EnumEntry with Snakecase

object Status extends Enum[Status] with CirceEnum[Status] {
  final case object New extends Status
  final case object InProgress extends Status
  final case object Completed extends Status
  override def values: IndexedSeq[Status] = findValues
}