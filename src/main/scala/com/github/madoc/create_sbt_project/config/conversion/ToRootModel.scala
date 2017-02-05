package com.github.madoc.create_sbt_project.config.conversion

import com.github.madoc.create_sbt_project.config._
import com.github.madoc.create_sbt_project.config.validation.{ConfigError, Validate}
import com.github.madoc.create_sbt_project.model._

object ToRootModel {
  def apply(config:RootConfig):Either[Seq[ConfigError],RootModel] = Validate(config) match {
    case Seq() ⇒ Right(RootModel(
      projectDirectory = config.project.get.directory getOrElse config.project.get.name.get,
      buildDefinition = ToBuildDefinition(config),
      buildProperties = ToBuildProperties(config),
      gitIgnore = ToGitIgnore(config),
      pluginsDefinition = ToPluginsDefinition(config)
    ))
    case errors ⇒ Left(errors)
  }
}
