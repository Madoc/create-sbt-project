package com.github.madoc.create_sbt_project.main

import com.github.madoc.create_sbt_project.action.precondition.PreconditionFailure
import com.github.madoc.create_sbt_project.cli.CommandLineResult._
import com.github.madoc.create_sbt_project.cli.ParseCommandLine
import com.github.madoc.create_sbt_project.config._
import com.github.madoc.create_sbt_project.config.validation.{ConfigError, Validate}
import com.github.madoc.create_sbt_project.io.FileSystemSupport
import com.github.madoc.create_sbt_project.main.MainProcessLogic.{ErrorsAfterApplyingCommandLineArguments, ErrorsInUserConfig, ErrorsWhileExecuting}
import spray.json._

class MainProcessLogic(fs:FileSystemSupport, commandLineArguments:Seq[String]) {
  lazy val userConfigOpt = fs.userPreferencesFileContents map {_.parseJson.convertTo[RootConfig]}

  def execute() = executionResult() match {
    case MainProcessLogic.Success ⇒ ()
    case ErrorsInUserConfig(errors) ⇒
      if(errors.size == 1) fs writeToErrorOutput "Error in user configuration:"
      else fs writeToErrorOutput "Errors in user configuration:"
      fs.writeToErrorOutput.lineBreak()
      fs writeToErrorOutput errors
    case ErrorsAfterApplyingCommandLineArguments(errors) ⇒ fs writeToErrorOutput errors
    case ErrorsWhileExecuting(errors) ⇒ fs writeToErrorOutput errors.distinct
  }

  private def executionResult():MainProcessLogic.Result =
    userConfigOpt map {cfg⇒Validate(cfg) filterNot {_ mightBeSolvedByCommandLineArgs}} match {
      case Some(errors) if errors nonEmpty ⇒ ErrorsInUserConfig(errors)
      case _ ⇒ parseAndExecuteCommandLine()
    }

  private def parseAndExecuteCommandLine():MainProcessLogic.Result = ParseCommandLine(commandLineArguments) match {
    case CommandLineErrors(errors) ⇒ ErrorsAfterApplyingCommandLineArguments(errors)
    case CreateSBTProject(modConfig) ⇒ DoCreateSBTProject(fs, modConfig(userConfigOpt?))
    case PrintCurrentConfig(modConfig) ⇒ DoPrintCurrentConfig(fs, modConfig(userConfigOpt?))
    case PrintHelp(writeWithWidth) ⇒ DoPrintHelp(fs, writeWithWidth)
    case PrintVersionInfo ⇒ DoPrintVersionInfo(fs)
  }
}
object MainProcessLogic {
  sealed trait Result
  case object Success extends Result
  sealed case class ErrorsInUserConfig(errors:Seq[ConfigError]) extends Result
  sealed case class ErrorsAfterApplyingCommandLineArguments(errors:Seq[ConfigError]) extends Result
  sealed case class ErrorsWhileExecuting(errors:Seq[PreconditionFailure]) extends Result
}
