package com.github.madoc.create_sbt_project.model

import com.github.madoc.create_sbt_project.model.base.IsEmpty
import com.github.madoc.create_sbt_project.model.output.ToStringBasedOnOutput

/** Models typical contents of a `plugins.sbt` file. */
case class PluginsDefinition(
  plugins:Set[LibraryDependency] = Set()
) extends ToStringBasedOnOutput[PluginsDefinition] with IsEmpty {def isEmpty = plugins isEmpty}
