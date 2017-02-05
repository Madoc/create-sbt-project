package com.github.madoc.create_sbt_project.config

import com.github.madoc.create_sbt_project.config.base.ProjectConfigBase

case class ProjectConfig(
  directory:Option[String] = None,
  name:Option[String] = None,
  organization:Option[String] = None,
  version:Option[String] = None,
  scalaVersion:Option[String] = None,
  sbtVersion:Option[String] = None,
  extraScalacOptions:Option[Seq[String]] = None,
  extraJavaOptions:Option[Set[JavaContextOptionConfig]] = None,
  extraBuildCommands:Option[Seq[String]] = None,
  gitIgnore:Option[Set[String]] = None,
  libraries:Option[Set[String]] = None,
  plugins:Option[Set[String]] = None
) extends ProjectConfigBase
