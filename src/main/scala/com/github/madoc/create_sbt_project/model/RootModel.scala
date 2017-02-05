package com.github.madoc.create_sbt_project.model

case class RootModel(
  projectDirectory:String,
  buildDefinition:BuildDefinition = BuildDefinition(),
  buildProperties:BuildProperties = BuildProperties(),
  gitIgnore:GitIgnore = GitIgnore(),
  pluginsDefinition:PluginsDefinition = PluginsDefinition()
)
