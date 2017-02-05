package com.github.madoc.create_sbt_project.config

case class JavaContextOptionConfig(
  contexts:Option[Set[String]] = None,
  options:Seq[String] = Seq()
)
