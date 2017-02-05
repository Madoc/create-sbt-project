package com.github.madoc.create_sbt_project.action.framework

import com.github.madoc.create_sbt_project.action.precondition.PreconditionFailure

sealed trait ActionResult {
  def followedBy(maybeNext: ⇒ActionResult):ActionResult
}
object ActionResult {
  object Success extends ActionResult {
    def followedBy(maybeNext: ⇒ActionResult) = maybeNext
  }
  sealed case class PreconditionFailures(failures:Seq[PreconditionFailure]) extends ActionResult {
    def followedBy(maybeNext: ⇒ActionResult) = this
  }
}
