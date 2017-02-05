package com.github.madoc.create_sbt_project.action

import com.github.madoc.create_sbt_project.model.RootModel
import org.scalatest.{FreeSpec, Matchers}

class CreateActionsTest extends FreeSpec with Matchers {
  "when a project directory ends with a slash, CreateActions will not generate any path with a double slash in it" in {
    CreateActions(RootModel(projectDirectory="projectdir/")).toSeq foreach {
      case OutputToFile(path,_,_) ⇒ (path contains "//") should be (false)
      case CreateDirectory(path) ⇒ (path contains "//") should be (false)
      case unexpected ⇒ sys error s"unexpected action type: $unexpected"
    }
  }
}
