package com.github.madoc.create_sbt_project.action.framework

import java.util.concurrent.atomic.AtomicBoolean

import com.github.madoc.create_sbt_project.action.CreateDirectory
import com.github.madoc.create_sbt_project.action.framework.ActionResult.PreconditionFailures
import com.github.madoc.create_sbt_project.action.precondition.PreconditionFailure
import com.github.madoc.create_sbt_project.action.precondition.PreconditionFailure.DirectoryDoesNotExist
import com.github.madoc.create_sbt_project.io.FileSystemSupport
import org.scalatest.{FreeSpec, Matchers}

class ActionListTest extends FreeSpec with Matchers {
  "an ActionList combines the precondition failures that its contents might deal with" in {
    val al = ActionList(CreateDirectory("dir1"), CreateDirectory("dir2"))
    al.mightDealWith(DirectoryDoesNotExist("dir1")) should be (true)
    al.mightDealWith(DirectoryDoesNotExist("dir2")) should be (true)
    al.mightDealWith(DirectoryDoesNotExist("dir3")) should be (false)
  }
  "ActionList() returns the empty action" in {
    ActionList() should be (ActionList empty)
  }
  "an empty ActionList cannot deal with any precondition failure" in {
    ActionList.empty.mightDealWith(DirectoryDoesNotExist("dir")) should be (false)
  }
  "an empty ActionList returns a meaningful toString" in {
    ActionList.empty.toString should be ("ActionList()")
  }
  "a non-empty ActionList returns a meaningful toString" in {
    ActionList(CreateDirectory("foo"), CreateDirectory("bar")).toString should be ("ActionList(CreateDirectory(foo), CreateDirectory(bar))")
  }
  "when an action in the middle of an ActionList fails, the following action is not called" in {
    object NonFailingAction extends Action {
      protected def run(env:ActionEnvironment) = ActionResult.Success
      def precondition = {_:ActionEnvironment ⇒ Seq()}
      def mightDealWith(failure:PreconditionFailure) = false
    }
    class FailingAction extends Action {
      val runCalled = new AtomicBoolean(false)
      protected def run(env:ActionEnvironment) = {
        runCalled set true
        ActionResult.PreconditionFailures(Seq(DirectoryDoesNotExist("dummy")))
      }
      def precondition = {_:ActionEnvironment ⇒ Seq()}
      def mightDealWith(failure:PreconditionFailure) = false
    }
    val (action1, action2, action3) = (NonFailingAction, new FailingAction, new FailingAction)
    ActionList(action1, action2, action3)(FileSystemSupport default) should be (PreconditionFailures(Seq(DirectoryDoesNotExist("dummy"))))
    action2.runCalled.get should be (true)
    action3.runCalled.get should be (false)
  }
}
