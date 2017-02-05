package com.github.madoc.create_sbt_project.config.conversion

import com.github.madoc.create_sbt_project.config._
import com.github.madoc.create_sbt_project.model.BuildProperties

private object ToBuildProperties {
  def apply(project:ProjectConfig):BuildProperties = BuildProperties(sbtVersion = project sbtVersion)

  def apply(rootConfig:RootConfig):BuildProperties = ToBuildProperties(rootConfig.project?)
}
