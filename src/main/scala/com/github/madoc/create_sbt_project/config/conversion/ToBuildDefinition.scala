package com.github.madoc.create_sbt_project.config.conversion

import com.github.madoc.create_sbt_project.config._
import com.github.madoc.create_sbt_project.model.BuildDefinition

private object ToBuildDefinition {
  def apply(project:ProjectConfig, refs:RefsConfig):BuildDefinition = BuildDefinition(
    name = project.name orElse project.directory,
    organization = project organization,
    version = project version,
    scalaVersion = project scalaVersion,
    extraScalacOptions = project.extraScalacOptions?,
    javaOptionContextsToOptions = ToJavaOptionContextsToOptions(project.extraJavaOptions?),
    additionalCommands = (project.extraBuildCommands?) ++ impliedCommandsFromPlugins(project, refs),
    libraryDependencies = ToLibraryDependency(project.libraries?, refs.libraryRefs?) toSeq,
    resolverURLsToNames = ToResolverMap(project, refs)
  )

  def apply(rootConfig:RootConfig):BuildDefinition = ToBuildDefinition(
    project = rootConfig.project?,
    refs = rootConfig.refs?
  )

  private def impliedCommandsFromPlugins(project:ProjectConfig, refs:RefsConfig):Seq[String] = {
    val explicitlyAddedPluginRefs:Set[String] = project.plugins?
    val impliedPluginRefs:Set[String] = ((project.libraries?) flatMap (refs.libraryRefs?)) flatMap {_.impliedPlugins?}
    val plugins:Set[PluginRefConfig] = (explicitlyAddedPluginRefs ++ impliedPluginRefs) flatMap (refs.pluginRefs?)
    plugins.toSeq flatMap {_.extraBuildCommands?}
  }
}
