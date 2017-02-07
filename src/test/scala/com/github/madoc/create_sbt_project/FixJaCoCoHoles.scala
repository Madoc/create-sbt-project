package com.github.madoc.create_sbt_project

import com.github.madoc.create_sbt_project.action.framework.ActionResult
import com.github.madoc.create_sbt_project.action.precondition.PreconditionFailure
import com.github.madoc.create_sbt_project.action.{CreateDirectory, OutputToFile}
import com.github.madoc.create_sbt_project.cli.CommandLineResult
import com.github.madoc.create_sbt_project.config.validation.ConfigError
import com.github.madoc.create_sbt_project.config.validation.ConfigError.{ErrorLocation, ErrorNature, SomethingToDefine}
import com.github.madoc.create_sbt_project.io.Write
import com.github.madoc.create_sbt_project.main.MainProcessLogic
import com.github.madoc.create_sbt_project.model._
import org.scalatest.FreeSpec

class FixJaCoCoHoles extends FreeSpec {
  "(fixing JaCoCo coverage holes)" in {Seq(
    ActionResult, ActionResult.PreconditionFailures,
    BuildDefinition, BuildDefinition(), BuildDefinition() copy(),
    BuildProperties() copy(),
    CommandLineResult, CommandLineResult.CreateSBTProject, CommandLineResult.PrintCurrentConfig, CommandLineResult.PrintHelp, CommandLineResult.PrintVersionInfo, CommandLineResult.CommandLineErrors,
    ConfigError,
    CreateDirectory,
    ErrorLocation, ErrorLocation.ConfigProperty, ErrorLocation.ElementOf, ErrorLocation.LibraryRefConfigLocation, ErrorLocation.PluginRefConfigLocation, ErrorLocation.ReferredBy, ErrorLocation.Unspecific,
    ErrorNature, ErrorNature.DuplicateJavaOptionContext, ErrorNature.ElementIsEmpty, ErrorNature.MissingDefinition, ErrorNature.MoreThanOneProjectDirectorySpeficied, ErrorNature.NeitherNameNorDirectoryIsSet, ErrorNature.WhitespaceAfter, ErrorNature.WhitespaceBefore,
    GitIgnore() copy(),
    LibraryDependency("","","") copy(),
    MainProcessLogic, MainProcessLogic.ErrorsAfterApplyingCommandLineArguments, MainProcessLogic.ErrorsInUserConfig, MainProcessLogic.ErrorsWhileExecuting,
    MainProcessLogic.ErrorsWhileExecuting(Seq()),
    OutputToFile,
    PluginsDefinition() copy(),
    PreconditionFailure, PreconditionFailure.DirectoryDoesNotExist, PreconditionFailure.DirectoryNotWritable, PreconditionFailure.FileIsOnDirectoryPath,
    RootModel, RootModel(""),
    SomethingToDefine,
    Write
  )}
}
