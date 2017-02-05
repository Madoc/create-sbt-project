package com.github.madoc.create_sbt_project.config.conversion

import com.github.madoc.create_sbt_project.config._
import com.github.madoc.create_sbt_project.model.LibraryDependency

private object ToLibraryDependency extends (PluginRefConfig⇒LibraryDependency) {
  def apply[A](libraries:Set[String], libraryRefs:Map[String,Set[A]])(implicit toLibraryDependency:ToLibraryDependency[A]):Set[LibraryDependency] =
    libraries flatMap libraryRefs map toLibraryDependency

  //  def apply(libraries:Set[String], libraryRefs:Map[String,Set[LibraryRefConfig]]):Seq[LibraryDependency] =
  //    libraries flatMap libraryRefs map {d⇒ToLibraryDependency(d dependency)} toSeq

  def apply(the:LibraryRefConfig):LibraryDependency = LibraryDependency(
    groupID = the group,
    artifactID = the artifact,
    revision = the version,
    addScalaVersion = the.addScalaVersion getOrElse true,
    configuration = the configuration,
    withSources = the.withSources getOrElse false
  )

  def apply(the:PluginRefConfig):LibraryDependency = LibraryDependency(
    groupID = the group,
    artifactID = the artifact,
    revision = the version,
    addScalaVersion = false
  )

  implicit val libraryRefConfigToLibraryDependency:ToLibraryDependency[LibraryRefConfig] =
    new ToLibraryDependency[LibraryRefConfig] {def apply(a:LibraryRefConfig) = ToLibraryDependency(a)}
  implicit val pluginRefConfigToLibraryDependency:ToLibraryDependency[PluginRefConfig] =
    new ToLibraryDependency[PluginRefConfig] {def apply(a:PluginRefConfig) = ToLibraryDependency(a)}
}
trait ToLibraryDependency[A] extends (A⇒LibraryDependency)
