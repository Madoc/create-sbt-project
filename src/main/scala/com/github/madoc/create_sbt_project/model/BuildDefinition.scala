package com.github.madoc.create_sbt_project.model

import com.github.madoc.create_sbt_project.model.output.ToStringBasedOnOutput

/** Models typical contents of a `build.sbt` file. */
case class BuildDefinition(
  name:Option[String] = None,
  organization:Option[String] = None,
  version:Option[String] = None,
  scalaVersion:Option[String] = None,
  extraScalacOptions:Seq[String] = Seq(),
  resolverURLsToNames:Map[String,String] = Map(),
  libraryDependencies:Seq[LibraryDependency] = Seq(),
  javaOptionContextsToOptions:Map[Set[String],Seq[String]] = Map(),
  additionalCommands:Seq[String] = Seq()
) extends ToStringBasedOnOutput[BuildDefinition]
