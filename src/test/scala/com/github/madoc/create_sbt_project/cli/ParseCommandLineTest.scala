package com.github.madoc.create_sbt_project.cli

import com.github.madoc.create_sbt_project.cli.CommandLineResult.CreateSBTProject
import com.github.madoc.create_sbt_project.config.RootConfig
import org.scalatest.{FreeSpec, Matchers}

class ParseCommandLineTest extends FreeSpec with Matchers {
  "parsing a null command line does not throw an error" in {
    val resultForNull = ParseCommandLine(null)
    resultForNull shouldBe a[CreateSBTProject]
    resultForNull match {
      case CreateSBTProject(modConfig) ⇒ modConfig(RootConfig()) should be (RootConfig())
      case _ ⇒ fail
    }
  }
}
