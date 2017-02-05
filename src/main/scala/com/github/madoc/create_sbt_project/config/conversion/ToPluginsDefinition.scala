package com.github.madoc.create_sbt_project.config.conversion

import com.github.madoc.create_sbt_project.config._
import com.github.madoc.create_sbt_project.model.PluginsDefinition

private object ToPluginsDefinition {
  def apply(project:ProjectConfig, refs:RefsConfig):PluginsDefinition = PluginsDefinition(
    ToLibraryDependency((project.plugins?) ++ impliedPlugins(project, refs), refs.pluginRefs?)
  )

  def apply(rootConfig:RootConfig):PluginsDefinition = ToPluginsDefinition(rootConfig.project?, rootConfig.refs?)

  private def impliedPlugins(project:ProjectConfig, refs:RefsConfig):Set[String] =
    ((project.libraries.? flatMap refs.libraryRefs.?) flatMap {_ impliedPlugins}).flatten
}
