package com.github.madoc.create_sbt_project.main

import com.github.madoc.create_sbt_project.config.RootConfig
import com.github.madoc.create_sbt_project.io.FileSystemSupport
import spray.json._

private object DoPrintCurrentConfig extends ((FileSystemSupport,RootConfig)⇒MainProcessLogic.Result) {
  def apply(fs:FileSystemSupport, cfg:RootConfig) = {
    fs.writeToStandardOutput(cfg.toJson.prettyPrint) lineBreak()
    MainProcessLogic.Success
  }
}
