package com.github.madoc.create_sbt_project.config

case class PluginRefConfig(
  group:String,
  artifact:String,
  version:String,
  resolverRef:Option[String] = None,
  extraBuildCommands:Option[Seq[String]] = None
)
