package com.github.madoc.create_sbt_project.config

import org.scalatest.{FreeSpec, Matchers}

class ProjectConfigTest extends FreeSpec with Matchers {
  "adding scalac options to a project config that already has scalac options will add more (and not overwrite)" in {
    val p1 = ProjectConfig(extraScalacOptions = Some(Seq("opt1", "opt2")))
    val p2 = p1.withAddedExtraScalacOptions(Seq("opt3", "opt4"))
    p2.extraScalacOptions should be (Some(Seq("opt1", "opt2", "opt3", "opt4")))
  }
}
