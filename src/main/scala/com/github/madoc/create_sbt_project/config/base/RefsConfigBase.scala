package com.github.madoc.create_sbt_project.config.base

import com.github.madoc.create_sbt_project.config.RefsConfig

trait RefsConfigBase {
  this:RefsConfig⇒

  def definesLibrary(libraryRef:String):Boolean = libraryRefs exists {_ contains libraryRef}
  def lacksLibrary(libraryRef:String):Boolean = !definesLibrary(libraryRef)

  def definesPlugin(pluginRef:String):Boolean = pluginRefs exists {_ contains pluginRef}
  def lacksPlugin(pluginRef:String):Boolean = !definesPlugin(pluginRef)

  def definesResolver(resolverRef:String):Boolean = resolverRefs exists {_ contains resolverRef}
  def lacksResolver(resolverRef:String):Boolean = !definesResolver(resolverRef)
  def lacksResolver(resolverRefOpt:Option[String]):Boolean = resolverRefOpt exists lacksResolver
}
