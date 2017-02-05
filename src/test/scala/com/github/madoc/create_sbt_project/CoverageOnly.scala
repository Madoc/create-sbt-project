package com.github.madoc.create_sbt_project

import com.github.madoc.create_sbt_project.action.framework.ActionResult
import com.github.madoc.create_sbt_project.action.precondition.PreconditionFailure
import com.github.madoc.create_sbt_project.action.{CreateDirectory, OutputToFile}
import com.github.madoc.create_sbt_project.cli.CommandLineResult
import com.github.madoc.create_sbt_project.io.Write
import com.github.madoc.create_sbt_project.main.MainProcessLogic
import com.github.madoc.create_sbt_project.model._
import org.scalatest.FreeSpec

class CoverageOnly extends FreeSpec {
  "(ensuring coverage of static constructs)" in {Seq(
    ActionResult, ActionResult.PreconditionFailures,
    BuildDefinition, BuildDefinition(), BuildDefinition() copy(),
    BuildProperties() copy(),
    CommandLineResult, CommandLineResult.CreateSBTProject, CommandLineResult.PrintCurrentConfig, CommandLineResult.PrintHelp, CommandLineResult.PrintVersionInfo, CommandLineResult.CommandLineErrors,
    CreateDirectory,
    GitIgnore() copy(),
    LibraryDependency("","","") copy(),
    MainProcessLogic, MainProcessLogic.ErrorsAfterApplyingCommandLineArguments, MainProcessLogic.ErrorsInUserConfig, MainProcessLogic.ErrorsWhileExecuting,
    MainProcessLogic.ErrorsWhileExecuting(Seq()),
    OutputToFile,
    PluginsDefinition() copy(),
    PreconditionFailure, PreconditionFailure.DirectoryDoesNotExist, PreconditionFailure.DirectoryNotWritable, PreconditionFailure.FileIsOnDirectoryPath,
    RootModel, RootModel(""),
    Write
  )}
}
