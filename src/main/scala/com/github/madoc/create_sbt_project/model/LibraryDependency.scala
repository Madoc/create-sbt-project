package com.github.madoc.create_sbt_project.model

import com.github.madoc.create_sbt_project.model.output.ToStringBasedOnOutput

case class LibraryDependency(
  groupID:String,
  artifactID:String,
  revision:String,
  addScalaVersion:Boolean = true,
  configuration:Option[String] = None,
  withSources:Boolean = false
) extends ToStringBasedOnOutput[LibraryDependency]
