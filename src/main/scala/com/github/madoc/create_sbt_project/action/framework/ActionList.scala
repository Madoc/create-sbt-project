package com.github.madoc.create_sbt_project.action.framework

import com.github.madoc.create_sbt_project.action.framework.ActionResult.Success
import com.github.madoc.create_sbt_project.action.precondition.PreconditionFailure

/** Single linked list of `Action`; is either empty or consists of head and tail. */
sealed trait ActionList extends Action {
  def toSeq:Seq[Action]
  protected def toStringSuffix:String
}
object ActionList {
  def apply(actions:Action*):ActionList = actions match {
    case Seq() ⇒ NilAction
    case Seq(head, tail@_*) ⇒ ConsAction(head, apply(tail:_*))
  }
  val empty:ActionList = NilAction
  def cons(head:Action, tail:ActionList):ActionList = ConsAction(head, tail)

  private object NilAction extends ActionList {
    protected def run(env:ActionEnvironment) = Success
    def precondition = {_:ActionEnvironment ⇒ Seq()}
    def mightDealWith(failure:PreconditionFailure) = false
    override def >>(that:Action) = ConsAction(that, NilAction)
    def toSeq = Seq()

    override def toString() = "ActionList()"
    protected def toStringSuffix = ")"
  }
  private sealed case class ConsAction(head:Action, tail:ActionList) extends ActionList {
    protected def run(env:ActionEnvironment) = head(env) followedBy tail(env)
    def precondition =
      {env:ActionEnvironment ⇒ tail.precondition(env).filterNot(head mightDealWith) ++ head.precondition(env)}
    def mightDealWith(failure:PreconditionFailure) = (head mightDealWith failure) || (tail mightDealWith failure)
    override def >>(that:Action) = if(that==NilAction) this else ConsAction(head, tail >> that)
    def toSeq = Seq(head) ++ tail.toSeq

    override def toString() = "ActionList(" + head + tail.toStringSuffix
    protected def toStringSuffix = ", " + head + tail.toStringSuffix
  }
}
