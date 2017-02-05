package com.github.madoc.create_sbt_project.cli

import com.github.madoc.create_sbt_project.config.RootConfig
import com.github.madoc.create_sbt_project.config.validation.ConfigError
import com.github.madoc.create_sbt_project.io.Write

sealed trait CommandLineResult
object CommandLineResult {
  sealed case class CreateSBTProject(modConfig:RootConfig⇒RootConfig) extends CommandLineResult
  sealed case class PrintCurrentConfig(modConfig:RootConfig⇒RootConfig) extends CommandLineResult
  sealed case class PrintHelp(writeWithWidth:Int⇒Write⇒Any) extends CommandLineResult
  object PrintVersionInfo extends CommandLineResult
  sealed case class CommandLineErrors(errors:Seq[ConfigError]) extends CommandLineResult
}
