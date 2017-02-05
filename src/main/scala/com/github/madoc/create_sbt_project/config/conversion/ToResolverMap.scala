package com.github.madoc.create_sbt_project.config.conversion

import com.github.madoc.create_sbt_project.config._

private object ToResolverMap {
  def apply(project:ProjectConfig, refs:RefsConfig):Map[String,String] =
    toResolverMap(allResolverRefs(project, refs), refs.resolverRefs?)

  private def toResolverMap(resolverRefs:Set[String], directory:Map[String,Set[ResolverRefConfig]]):Map[String,String] =
    resolverRefs flatMap directory map {r ⇒ (r name, r url)} toMap

  private def allResolverRefs(project:ProjectConfig, refs:RefsConfig):Set[String] =
    resolverRefsOf(project.libraries?, refs.libraryRefs?) ++ resolverRefsOf(project.plugins?, refs.pluginRefs?)

  private def resolverRefsOf[A](refs:Set[String], directory:Map[String,Set[A]])(implicit rr:ResolverRef[A]):Set[String] =
    resolverRefsOf(refs flatMap directory)
  private def resolverRefsOf[A](those:Set[A])(implicit rr:ResolverRef[A]):Set[String] = those flatMap rr

  private trait ResolverRef[A] extends (A⇒Seq[String])
  private implicit val libraryRefConfigResolverRef:ResolverRef[LibraryRefConfig] =
    new ResolverRef[LibraryRefConfig] {def apply(a:LibraryRefConfig) = a.resolverRef toSeq}
  private implicit val pluginRefConfigResolverRef:ResolverRef[PluginRefConfig] =
    new ResolverRef[PluginRefConfig] {def apply(a:PluginRefConfig) = a.resolverRef toSeq}
}
