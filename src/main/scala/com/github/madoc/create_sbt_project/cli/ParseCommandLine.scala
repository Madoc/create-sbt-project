package com.github.madoc.create_sbt_project.cli

import java.io.PrintWriter

import com.github.madoc.create_sbt_project.cli.CommandLineResult._
import com.github.madoc.create_sbt_project.config.validation.ConfigError.ErrorLocation.CommandLineArgument
import com.github.madoc.create_sbt_project.config.validation.ConfigError.ErrorNature.{MoreThanOneProjectDirectorySpeficied, UnrecognizedCommandLineOption}
import com.github.madoc.create_sbt_project.config.{ExampleConfiguration, RootConfig}
import org.apache.commons.cli.{CommandLine, DefaultParser, Options, UnrecognizedOptionException}

object ParseCommandLine {
  def apply(args:Seq[String]):CommandLineResult = if(args==null) CreateSBTProject(identity) else try {
    val cl = parse(args)
    if((cl hasOption "help")||(cl hasOption '?')) PrintHelp {width:Int⇒ write⇒
      val formatter = new CSPHelpFormatter
      formatter.setWidth(width)
      formatter.printHelp(new PrintWriter(write asJavaIOWriter), width, "create-sbt-project", null, options,
        formatter getLeftPadding, formatter getDescPadding, null, true)
    }
    else if(cl hasOption 'v') PrintVersionInfo
    else if(cl hasOption 'V') modConfig(cl)(PrintCurrentConfig)
    else modConfig(cl)(CreateSBTProject)
  } catch {
    case unrecognized:UnrecognizedOptionException ⇒
      CommandLineErrors(Seq(UnrecognizedCommandLineOption asErrorAt CommandLineArgument(unrecognized getOption)))
  }

  private def parse(args:Seq[String]):CommandLine = new DefaultParser().parse(options, args toArray)

  private def options:Options = new Options()
    .addOption("b", "sbt-version", true, "SBT version for the project")
    .addOption("c", "sbt-command", true, "add an SBT command to be executed during builds")
    .addOption("g", "git-ignore", true, "add a pattern to .gitignore")
    .addOption("h", "help", false, "print this message and exit")
    .addOption("i", "example-config", false, "load internal example configuration; may be useful with --print-config")
    .addOption("l", "library", true, "add a library dependency to the project, by providing the symbolic name (\"library ref\")")
    .addOption("n", "name", true, "SBT project name (part of the artifact ID); default: directory")
    .addOption("o", "organization", true, "'organization' part of the new artifact ID")
    .addOption("p", "plugin", true, "add an SBT plugin to the project, by providing the symbolic name (\"plugin refs\")")
    .addOption("r", "revision", true, "'version' part of the artifact ID")
    .addOption("s", "scala-version", true, "scala version for the project")
    .addOption("v", "version", false, "print the version information and exit")
    .addOption("V", "print-config", false, "print current config, after applying all other arguments, and exit")
    .addOption("x", "extra-scalac-option", true, "add one option to supply to the scalac command on the command line")
    .addOption("?", false, "same as --help")

  private def modConfig(cl:CommandLine)(wrap:(RootConfig⇒RootConfig)⇒CommandLineResult):CommandLineResult = {
    var result:RootConfig⇒RootConfig = identity
    def cfg(f:RootConfig⇒RootConfig) {result = result andThen f}
    def singleValue(longOption:String, f:(RootConfig,String)⇒RootConfig)
      {Option(cl getOptionValues longOption) flatMap {_ lastOption} foreach {v ⇒ cfg(c ⇒ f(c,v))}}
    def multiValue(longOption:String, f:(RootConfig,Seq[String])⇒RootConfig)
      {Option(cl getOptionValues longOption) map {_ toSeq} foreach {v ⇒ cfg(c ⇒ f(c,v))}}

    if(cl hasOption "example-config") result = {_ ⇒ ExampleConfiguration root}

    singleValue("sbt-version", _ withSBTVersion _)
    multiValue("sbt-command", _ withAddedAdditionalCommands _)
    multiValue("git-ignore", _ withAddedGitIgnorePatterns _)
    multiValue("library", _ withAddedLibraryRefs _)
    singleValue("name", _ withName _)
    singleValue("organization", _ withOrganization _)
    multiValue("plugin", _ withAddedPluginRefs _)
    singleValue("revision", _ withVersion _)
    singleValue("scala-version", _ withScalaVersion _)
    multiValue("extra-scalac-option", _ withAddedExtraScalacOptions _)

    cl.getArgs.toSeq match {
      case Seq() ⇒ wrap(result)
      case Seq(directory) ⇒ cfg(_ withExplicitDirectory directory); wrap(result)
      case _ ⇒ CommandLineErrors(Seq(MoreThanOneProjectDirectorySpeficied error))
    }
  }
}
