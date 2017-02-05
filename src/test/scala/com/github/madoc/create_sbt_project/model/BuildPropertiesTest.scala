package com.github.madoc.create_sbt_project.model

import org.scalatest.{FreeSpec, Matchers}

class BuildPropertiesTest extends FreeSpec with Matchers {
  "basic test" in {
    val bp = BuildProperties(sbtVersion=Some("0.13.12"))
    bp.toString should be ("sbt.version=0.13.12\n")
  }
}
