package com.github.madoc.create_sbt_project.config

import com.github.madoc.create_sbt_project.config.base.RootConfigBase

case class RootConfig(
  project:Option[ProjectConfig] = None,
  refs:Option[RefsConfig] = None
) extends RootConfigBase
