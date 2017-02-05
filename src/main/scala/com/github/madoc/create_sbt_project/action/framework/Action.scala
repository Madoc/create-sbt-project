package com.github.madoc.create_sbt_project.action.framework

import com.github.madoc.create_sbt_project.action.framework.ActionResult.PreconditionFailures
import com.github.madoc.create_sbt_project.action.precondition.{Precondition, PreconditionFailure}

/** One of the things that this program can do. Actions can check their preconditions and execute themselves. */
trait Action {
  final def apply(env:ActionEnvironment):ActionResult = precondition(env) match {
    case Seq() ⇒ run(env)
    case failures ⇒ PreconditionFailures(failures)
  }
  protected def run(env:ActionEnvironment):ActionResult
  def precondition:Precondition
  def mightDealWith(failure:PreconditionFailure):Boolean

  def >>(that:Action):ActionList = that match {
    case thatList:ActionList ⇒ ActionList.cons(this, thatList)
    case _ ⇒ ActionList.cons(this, ActionList.cons(that, ActionList.empty))
  }
}
