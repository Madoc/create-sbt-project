package com.github.madoc.create_sbt_project.main

import java.nio.charset.Charset

import com.github.madoc.create_sbt_project.config.{ExampleConfiguration, RootConfig}
import com.github.madoc.create_sbt_project.io.{Output, TestFileSystemSupport, Write}
import org.scalatest.{FreeSpec, Matchers}
import spray.json._

class TestWithoutUserConfig extends FreeSpec with Matchers with MainTestTools {
  "creating a project with only a name" in {
    val fs = runWithNoUserConfig("-n", "testproject")(noStandardOutput=true, noErrorOutput=true)
    fs.getFileContents("testproject/build.sbt").trim should be ("""name := "testproject"""")
    fs.fileExists("testproject/project") should be (false)
    fs.fileExists("testproject/.gitignore") should be (false)
  }
  "creating a project with only a directory" in {
    val fs = runWithNoUserConfig("testproject")(noStandardOutput=true, noErrorOutput=true)
    fs.getFileContents("testproject/build.sbt").trim should be ("""name := "testproject"""")
    fs.fileExists("testproject/project") should be (false)
    fs.fileExists("testproject/.gitignore") should be (false)
  }
  "creating a project with both a name and a directory" in {
    val fs = runWithNoUserConfig("-n", "projectname", "testproject")(noStandardOutput=true, noErrorOutput=true)
    fs.getFileContents("testproject/build.sbt").trim should be ("""name := "projectname"""")
    fs.fileExists("testproject/project") should be (false)
    fs.fileExists("testproject/.gitignore") should be (false)
  }
  "creating a project with all command-line options that can be added without a user config" in {
    val fs = runWithNoUserConfig("-n", "projectname", "-o", "com.github.madoc", "-r", "0.1-SNAPSHOT",
      "--scala-version", "2.10.2", "--sbt-version", "0.13.12", "-x", "some-opt", "--sbt-command", "jacoco.settings",
      "--git-ignore", ".DS_Store", "--", "testproject")(noStandardOutput=true, noErrorOutput=true)
    fs.getFileContents("testproject/build.sbt").trim should be (
      """
        |organization := "com.github.madoc"
        |
        |name := "projectname"
        |
        |version := "0.1-SNAPSHOT"
        |
        |scalaVersion := "2.10.2"
        |
        |scalacOptions += "some-opt"
        |
        |jacoco.settings
      """.stripMargin.trim)
    fs.getFileContents("testproject/.gitignore").trim should be (".DS_Store")
    fs.getFileContents("testproject/project/build.properties").trim should be ("sbt.version=0.13.12")
    fs.fileExists("testproject/project/plugins.sbt") should be (false)
  }
  "creating a project fails if there is a file with the same name as the target directory" in {
    val fs = new TestFileSystemSupport()
    fs.outputToFile("file contents", "a-file-is-here", Charset forName "utf-8")(new Output[String] {
      def apply(the:String)(write:Write) = write(the)
    })
    new Main(fs, Seq("a-file-is-here"))
    fs.getStandardOutput should be ('empty)
    fs.getErrorOutput.trim should be(
      """
        |A file exists on path "a-file-is-here", but a directory is supposed to be there.
      """.stripMargin.trim)
  }
  "writing a simple command-line based config to standard output yields the desired JSON" in {
    runWithNoUserConfig("-b", "0.13.13", "-g", "gitignore1", "-g", "gitignore2", "-n", "projectname",
      "-o", "com.mycompany", "-r", "0.1-SNAPSHOT", "-s", "2.10.2", "-V")(noErrorOutput=true).getStandardOutput.trim should be (
        """
          |{
          |  "project": {
          |    "organization": "com.mycompany",
          |    "name": "projectname",
          |    "scalaVersion": "2.10.2",
          |    "version": "0.1-SNAPSHOT",
          |    "gitIgnore": ["gitignore1", "gitignore2"],
          |    "sbtVersion": "0.13.13"
          |  }
          |}
        """.stripMargin.trim)
  }
  "creating a project in a non-writable path yields an error message" in {
    val fs = new TestFileSystemSupport() {
      override def isFileWritable(path:String) = if(path=="nonwritable") false else true
    }
    new Main(fs, Seq("nonwritable"))
    fs.getStandardOutput should be ('empty)
    fs.getErrorOutput.trim should be ("""Directory "nonwritable" is not writable.""")
  }
  "setting the SBT version multiple times uses the last set value" in {
    val fs = runWithNoUserConfig("-b", "0.13.0", "-sbt-version", "0.13.3", "-b", "0.13.12", "testproject")(noStandardOutput=true, noErrorOutput=true)
    fs.getFileContents("testproject/project/build.properties").trim should be ("sbt.version=0.13.12")
  }
  "setting the options for loading and printing the example configuration will do just that" in {
    val output = runWithNoUserConfig("--example-config", "--print-config")(noErrorOutput=true).getStandardOutput
    output.parseJson.convertTo[RootConfig] should be (ExampleConfiguration root)
  }
  "using an unknown command-line argument does not throw an exception" in {
    runWithNoUserConfig("--unknown-command")(noStandardOutput=true)
  }
}
