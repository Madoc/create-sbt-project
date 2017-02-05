package com.github.madoc.create_sbt_project.main

import com.github.madoc.create_sbt_project.io.{FileSystemSupport, TestFileSystemSupport}
import org.scalatest.{FreeSpec, Matchers}

class TestCLIErrors extends FreeSpec with Matchers with MainTestTools {
  "adding a missing library reference leads to the appropriate error message" in {
    val fs = runWithNoUserConfig("-l", "foolib", "--", "testproject")(noStandardOutput = true)
    fs.getErrorOutput.trim should be("""Missing definition for library "foolib".""")
  }
  "adding a missing plugin reference leads to the appropriate error message" in {
    val fs = runWithNoUserConfig("-p", "fooplugin", "--", "testproject")(noStandardOutput = true)
    fs.getErrorOutput.trim should be("""Missing definition for plugin "fooplugin".""")
  }
  "leaving out both project name and directory leads to the appropriate error message" in {
    val fs = new TestFileSystemSupport()
    val prevFS = FileSystemSupport.main.get
    FileSystemSupport.main.set(fs)
    try Main.main(Array.empty[String]) finally FileSystemSupport.main.set(prevFS)
    fs.getErrorOutput.trim should be("""Neither project name nor project directory is set, but one of the two must be given.""")
  }
  "specifying more than one project directory leads to the appropriate error message" in {
    val fs = runWithNoUserConfig("testproject1", "testproject2")(noStandardOutput = true)
    fs.getErrorOutput.trim should be("""More than one project directory specified.""")
  }
  "whitespace in the project directory leads to an error" in {
    val fs = runWithNoUserConfig(" ws")(noStandardOutput=true)
    fs.getErrorOutput.trim should be ("""Whitespace before project directory.""")
  }
  "empty project organization (as opposed to left out) leads to an error" in {
    val fs = runWithNoUserConfig("-o", "", "testproject")(noStandardOutput=true)
    fs.getErrorOutput.trim should be ("""Empty project organization.""")
  }
}
