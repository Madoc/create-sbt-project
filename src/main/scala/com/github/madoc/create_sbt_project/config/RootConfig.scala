package com.github.madoc.create_sbt_project.config

import com.github.madoc.create_sbt_project.config.base.RootConfigBase

// TODO check the config keys, and refactor some of them to be more concise
case class RootConfig(
  project:Option[ProjectConfig] = None,
  refs:Option[RefsConfig] = None
) extends RootConfigBase
