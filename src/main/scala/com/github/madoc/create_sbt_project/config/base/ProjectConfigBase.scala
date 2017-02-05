package com.github.madoc.create_sbt_project.config.base

import com.github.madoc.create_sbt_project.config.ProjectConfig

trait ProjectConfigBase {
  this:ProjectConfig⇒

  def withAddedAdditionalCommands(additionalCommands:Iterable[String]):ProjectConfig =
    withAddedToSeq(additionalCommands, _ extraBuildCommands, {x:Option[Seq[String]]⇒copy(extraBuildCommands = x)})
  def withAddedExtraScalacOptions(extraScalacOptions:Iterable[String]):ProjectConfig =
    withAddedToSeq(extraScalacOptions, _ extraScalacOptions, {x:Option[Seq[String]]⇒copy(extraScalacOptions = x)})
  def withAddedGitIgnorePatterns(gitIgnorePatterns:Iterable[String]):ProjectConfig =
    withAddedToSet(gitIgnorePatterns, _ gitIgnore, {x:Option[Set[String]]⇒copy(gitIgnore = x)})
  def withAddedLibraryRefs(libraryRefs:Iterable[String]):ProjectConfig =
    withAddedToSet(libraryRefs, _ libraries, {x:Option[Set[String]]⇒copy(libraries = x)})
  def withAddedPluginRefs(pluginRefs:Iterable[String]):ProjectConfig =
    withAddedToSet(pluginRefs, _ plugins, {x:Option[Set[String]]⇒copy(plugins = x)})

  protected def withAddedToSet[A](toAdd:Iterable[A], get:ProjectConfig⇒Option[Set[A]], set:Option[Set[A]]⇒ProjectConfig):ProjectConfig =
    get(this) match {
      case Some(vs) ⇒ set(Some(vs ++ toAdd))
      case None ⇒ set(Some(toAdd toSet))
    }
  protected def withAddedToSeq[A](toAdd:Iterable[A], get:ProjectConfig⇒Option[Seq[A]], set:Option[Seq[A]]⇒ProjectConfig):ProjectConfig =
    get(this) match {
      case Some(vs) ⇒ set(Some(vs ++ toAdd))
      case None ⇒ set(Some(toAdd toSeq))
    }
}
