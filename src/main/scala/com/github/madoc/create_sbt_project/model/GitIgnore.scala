package com.github.madoc.create_sbt_project.model

import com.github.madoc.create_sbt_project.model.base.IsEmpty
import com.github.madoc.create_sbt_project.model.output.ToStringBasedOnOutput

/** The contents of a `.gitignore` file. */
case class GitIgnore(
  entries:Set[String] = Set()
) extends ToStringBasedOnOutput[GitIgnore] with IsEmpty {def isEmpty = entries isEmpty}
