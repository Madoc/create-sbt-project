package com.github.madoc.create_sbt_project.config

case class LibraryRefConfig(
  group:String,
  artifact:String,
  version:String,
  addScalaVersion:Option[Boolean] = None,
  configuration:Option[String] = None,
  withSources:Option[Boolean] = None,
  resolverRef:Option[String] = None,
  impliedPlugins:Option[Set[String]] = None
)
