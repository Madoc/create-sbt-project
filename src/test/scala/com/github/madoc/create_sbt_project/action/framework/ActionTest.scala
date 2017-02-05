package com.github.madoc.create_sbt_project.action.framework

import com.github.madoc.create_sbt_project.action.CreateDirectory
import com.github.madoc.create_sbt_project.action.framework.ActionResult.PreconditionFailures
import com.github.madoc.create_sbt_project.action.precondition.PreconditionFailure
import com.github.madoc.create_sbt_project.action.precondition.PreconditionFailure.DirectoryDoesNotExist
import com.github.madoc.create_sbt_project.io.FileSystemSupport
import org.scalatest.{FreeSpec, Matchers}

class ActionTest extends FreeSpec with Matchers {
  "adding an action list to a simple action returns an action list" in {
    val actionList = ActionList(CreateDirectory("dir1"), CreateDirectory("dir2"))
    val simpleAction = CreateDirectory("dir3")
    (simpleAction >> actionList) should be (ActionList(CreateDirectory("dir3"), CreateDirectory("dir1"), CreateDirectory("dir2")))
  }
  "if an action's precondition returns a failure, executing the action will result in failure" in {
    object ActionWithPreconditionFailure extends Action {
      protected def run(env:ActionEnvironment) = sys error "should not be called because there is a precondition failure"
      def precondition = {_:ActionEnvironment ⇒ Seq(DirectoryDoesNotExist("dummy"))}
      def mightDealWith(failure:PreconditionFailure) = false
    }
    ActionWithPreconditionFailure(FileSystemSupport default) should be (PreconditionFailures(Seq(DirectoryDoesNotExist("dummy"))))
  }
}
