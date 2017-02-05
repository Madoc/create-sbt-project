package com.github.madoc.create_sbt_project.config.conversion

import com.github.madoc.create_sbt_project.config._
import com.github.madoc.create_sbt_project.model.GitIgnore

private object ToGitIgnore {
  def apply(rootConfig:RootConfig):GitIgnore = GitIgnore((rootConfig.project.? gitIgnore)?)
}
