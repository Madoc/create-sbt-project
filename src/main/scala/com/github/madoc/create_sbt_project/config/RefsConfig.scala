package com.github.madoc.create_sbt_project.config

import com.github.madoc.create_sbt_project.config.base.RefsConfigBase

case class RefsConfig(
  libraryRefs:Option[Map[String,Set[LibraryRefConfig]]] = None,
  pluginRefs:Option[Map[String,Set[PluginRefConfig]]] = None,
  resolverRefs:Option[Map[String,Set[ResolverRefConfig]]] = None
) extends RefsConfigBase
