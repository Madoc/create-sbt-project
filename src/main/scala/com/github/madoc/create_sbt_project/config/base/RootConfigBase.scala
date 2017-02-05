package com.github.madoc.create_sbt_project.config.base

import com.github.madoc.create_sbt_project.config.{ProjectConfig, RootConfig}

trait RootConfigBase {
  this:RootConfig⇒

  protected def modProjectConfig(f:ProjectConfig⇒ProjectConfig):RootConfig = copy(project = Some(f(project?)))
  def withAddedAdditionalCommands(additionalCommands:Iterable[String]):RootConfig = modProjectConfig(_ withAddedAdditionalCommands additionalCommands)
  def withAddedExtraScalacOptions(extraScalacOptions:Iterable[String]):RootConfig = modProjectConfig(_ withAddedExtraScalacOptions extraScalacOptions)
  def withAddedGitIgnorePatterns(gitIgnorePatterns:Iterable[String]):RootConfig = modProjectConfig(_ withAddedGitIgnorePatterns gitIgnorePatterns)
  def withAddedLibraryRefs(libraryRefs:Iterable[String]):RootConfig = modProjectConfig(_ withAddedLibraryRefs libraryRefs)
  def withAddedPluginRefs(pluginRefs:Iterable[String]):RootConfig = modProjectConfig(_ withAddedPluginRefs pluginRefs)
  def withExplicitDirectory(directory:String):RootConfig = modProjectConfig(_ copy(directory=Some(directory)))
  def withName(name:String):RootConfig = modProjectConfig(_ copy(name=Some(name)))
  def withOrganization(organization:String):RootConfig = modProjectConfig(_ copy(organization=Some(organization)))
  def withSBTVersion(sbtVersion:String):RootConfig = modProjectConfig(_ copy(sbtVersion=Some(sbtVersion)))
  def withScalaVersion(scalaVersion:String):RootConfig = modProjectConfig(_ copy(scalaVersion=Some(scalaVersion)))
  def withVersion(version:String):RootConfig = modProjectConfig(_ copy(version=Some(version)))
}
