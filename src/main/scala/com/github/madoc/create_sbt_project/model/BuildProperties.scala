package com.github.madoc.create_sbt_project.model

import com.github.madoc.create_sbt_project.model.base.IsEmpty
import com.github.madoc.create_sbt_project.model.output.ToStringBasedOnOutput

case class BuildProperties(
  sbtVersion:Option[String] = None
) extends ToStringBasedOnOutput[BuildProperties] with IsEmpty {def isEmpty = sbtVersion isEmpty}
