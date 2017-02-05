package com.github.madoc.create_sbt_project.main

import com.github.madoc.create_sbt_project.version.BuildInfo
import org.scalatest.{FreeSpec, Matchers}

class TestHelpAndVersion extends FreeSpec with Matchers with MainTestTools {
  "version option just prints the version info and exit" in {
    mockSystemProperties(
      "java.runtime.name" → "Java Test Environment",
      "java.runtime.version" → "1.8.0_testbuild",
      "java.vm.name" → "Java Test VM",
      "java.vm.version" → "25.25-test",
      "java.vm.info" → "unit test mode"
    ) {
      runWithNoUserConfig("-v")(noErrorOutput=true).getStandardOutput.trim should be (
        """
          |create-sbt-project 0.1-SNAPSHOT
          |Java Test Environment (build 1.8.0_testbuild)
          |Java Test VM (build 25.25-test, unit test mode)
        """.stripMargin.trim)
    }
    mockSystemProperties(
      "java.runtime.name" → null,
      "java.vm.name" → null
    ) {
      runWithNoUserConfig("-v")(noErrorOutput=true).getStandardOutput.trim should be (
        """
          |create-sbt-project 0.1-SNAPSHOT
        """.stripMargin.trim)
    }
    mockSystemProperties(
      "java.runtime.name" → "Java Test Environment",
      "java.runtime.version" → null,
      "java.vm.name" → "Java Test VM",
      "java.vm.version" → null
    ) {
      runWithNoUserConfig("-v")(noErrorOutput=true).getStandardOutput.trim should be (
        s"""
          |create-sbt-project ${BuildInfo version}
          |Java Test Environment
          |Java Test VM
        """.stripMargin.trim)
    }
    mockSystemProperties(
      "java.runtime.name" → null,
      "java.vm.name" → "Java Test VM",
      "java.vm.version" → "25.25-test",
      "java.vm.info" → null
    ) {
      runWithNoUserConfig("-v")(noErrorOutput=true).getStandardOutput.trim should be (
        """
          |create-sbt-project 0.1-SNAPSHOT
          |Java Test VM (build 25.25-test)
        """.stripMargin.trim)
    }
  }
  "help option just prints the usage and exits" in {
    runWithNoUserConfig("--help")(noErrorOutput=true).getStandardOutput should startWith("usage: create-sbt-project")
  }
}
