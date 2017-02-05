package com.github.madoc.create_sbt_project.config.conversion

import com.github.madoc.create_sbt_project.config._

private object ToJavaOptionContextsToOptions {
  def apply(configs:Set[JavaContextOptionConfig]):Map[Set[String],Seq[String]] = configs map toMapEntry toMap

  private def toMapEntry(the:JavaContextOptionConfig):(Set[String],Seq[String]) = (the.contexts?, the options)
}
