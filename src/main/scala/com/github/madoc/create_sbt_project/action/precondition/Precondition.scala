package com.github.madoc.create_sbt_project.action.precondition

import com.github.madoc.create_sbt_project.action.framework.ActionEnvironment

trait Precondition extends (ActionEnvironment⇒Seq[PreconditionFailure])
object Precondition {
  implicit def apply(f:ActionEnvironment⇒Seq[PreconditionFailure]):Precondition = new Precondition {
    def apply(in:ActionEnvironment) = f(in)
  }
}
