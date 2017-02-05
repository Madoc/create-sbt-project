package com.github.madoc.create_sbt_project.main

import com.github.madoc.create_sbt_project.action.CreateActions
import com.github.madoc.create_sbt_project.action.framework.ActionResult
import com.github.madoc.create_sbt_project.config.RootConfig
import com.github.madoc.create_sbt_project.config.conversion.ToRootModel
import com.github.madoc.create_sbt_project.io.FileSystemSupport
import com.github.madoc.create_sbt_project.main.MainProcessLogic.{ErrorsAfterApplyingCommandLineArguments, ErrorsWhileExecuting}

private object DoCreateSBTProject extends ((FileSystemSupport,RootConfig)⇒MainProcessLogic.Result) {
  def apply(fs:FileSystemSupport, cfg:RootConfig) = ToRootModel(cfg) match {
    case Left(errors) ⇒ ErrorsAfterApplyingCommandLineArguments(errors)
    case Right(rootModel) ⇒ CreateActions(rootModel)(fs) match {
      case ActionResult.Success ⇒ MainProcessLogic.Success
      case ActionResult.PreconditionFailures(errors) ⇒ ErrorsWhileExecuting(errors)
    }
  }
}
