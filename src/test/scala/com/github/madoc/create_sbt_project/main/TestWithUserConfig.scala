package com.github.madoc.create_sbt_project.main

import org.scalatest.{FreeSpec, Matchers}

class TestWithUserConfig extends FreeSpec with Matchers with MainTestTools {
  "missing plugin ref for library is reported correctly" in {
    runWithUserConfig(
      """
        |{"refs": {
        |  "libraryRefs": {
        |    "testlib": [
        |      {
        |        "group":"com.github.madoc", "artifact":"testdep", "version":"0.1-SNAPSHOT",
        |        "impliedPlugins": ["non-existing-plugin"]
        |      }
        |    ]
        |}}}
      """.stripMargin)("testproject")(noStandardOutput=true).getErrorOutput.trim should be("""
        |Error in user configuration:
        |Missing definition for plugin "non-existing-plugin", as referred to in library definition: "com.github.madoc" %% "testdep" % "0.1-SNAPSHOT"
      """.stripMargin.trim)
  }
  "missing resolver ref within a library definition is reported correctly" in {
    runWithUserConfig(
      """
        |{"refs": {
        |  "libraryRefs": {
        |    "testlib": [
        |      {
        |        "group":"com.github.madoc", "artifact":"testdep", "version":"0.1-SNAPSHOT",
        |        "resolverRef": "non-existing-resolver"
        |      }
        |    ]
        |}}}
      """.stripMargin)("testproject")(noStandardOutput=true).getErrorOutput.trim should be("""
        |Error in user configuration:
        |Missing definition for resolver "non-existing-resolver", as referred to in library definition: "com.github.madoc" %% "testdep" % "0.1-SNAPSHOT"
      """.stripMargin.trim)
  }
  "missing resolver ref within a plugin definition is reported correctly" in {
    runWithUserConfig(
      """
        |{"refs": {
        |  "pluginRefs": {
        |    "testplugin": [
        |      {
        |        "group":"com.github.madoc", "artifact":"testplugin", "version":"0.1-SNAPSHOT",
        |        "resolverRef": "non-existing-resolver"
        |      }
        |    ]
        |}}}
      """.stripMargin)("testproject")(noStandardOutput=true).getErrorOutput.trim should be("""
        |Error in user configuration:
        |Missing definition for resolver "non-existing-resolver", as referred to in plugin definition: "com.github.madoc" % "testplugin" % "0.1-SNAPSHOT"
      """.stripMargin.trim)
  }
  "larger case testing most of the features" in {
    val fs = runWithUserConfig(
      """
        |{
        | "project": {
        |   "explicitDirectory": "gets_overwritten_by_command_line",
        |   "name": "gets_overwritten_by_command_line",
        |   "organization": "com.github.madoc",
        |   "version": "0.1-SNAPSHOT",
        |   "scalaVersion": "2.12.1",
        |   "sbtVersion": "0.13.13",
        |   "extraScalacOptions": ["-language:_", "-unchecked", "-deprecation", "-encoding", "utf8"],
        |   "extraJavaOptions": [
        |     {"options": ["-Xdebug", "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"]},
        |     {
        |       "contexts": ["Test", "run"],
        |       "options": ["-Djava.awt.headless=true"]
        |     }
        |   ],
        |   "extraBuildCommands": ["jacoco.settings"],
        |   "gitIgnore": [
        |     ".idea",
        |     "project/target",
        |     "project/project"
        |   ],
        |   "libraries": ["scalatest"],
        |   "plugins": ["jacoco"]
        | },
        | "refs": {
        |   "libraryRefs": {
        |     "scalatest": [{
        |       "group": "org.scalatest", "artifact": "scalatest", "version": "3.0.0",
        |       "configuration": "test", "withSources": true
        |     }],
        |     "spray-json": [{
        |       "group": "io.spray", "artifact": "spray-json", "version": "1.3.2",
        |       "withSources": true, "resolverRef": "sonatype-releases"
        |     }]
        |   },
        |   "pluginRefs": {
        |     "jacoco": [{
        |       "group": "de.johoop", "artifact": "jacoco4sbt", "version": "2.2.0",
        |       "resolverRef": "imaginary-resolver"
        |     }],
        |     "scoverage": [{
        |       "group": "org.scoverage", "artifact": "sbt-scoverage", "version": "1.5.0"
        |     }]
        |   },
        |   "resolverRefs": {
        |     "sonatype-releases": [{
        |       "name": "Sonatype Releases",
        |       "url": "http://oss.sonatype.org/content/repositories/releases"
        |     }],
        |     "imaginary-resolver": [{
        |       "name": "Imaginary Resolver",
        |       "url": "http://imaginary-resolver.org/content/repositories/releases"
        |     }]
        |   }
        | }
        |}
      """.stripMargin)("-l", "spray-json", "-p", "scoverage", "--name", "larger-testproject", "testproject")(noStandardOutput=true, noErrorOutput=true)
    fs.fileExists("testproject") should be (true)
    fs.fileExists("testproject/.gitignore") should be (true)
    fs.fileExists("testproject/build.sbt") should be (true)
    fs.fileExists("testproject/project") should be (true)
    fs.fileExists("testproject/project/build.properties") should be (true)
    fs.fileExists("testproject/project/plugins.sbt") should be (true)
    fs.getFileContents("testproject/.gitignore").trim should be (
      """
        |.idea
        |project/project
        |project/target
      """.stripMargin.trim)
    fs.getFileContents("testproject/build.sbt").trim should be (
      """
        |organization := "com.github.madoc"
        |
        |name := "larger-testproject"
        |
        |version := "0.1-SNAPSHOT"
        |
        |scalaVersion := "2.12.1"
        |
        |scalacOptions ++= Seq("-language:_", "-unchecked", "-deprecation", "-encoding", "utf8")
        |
        |resolvers ++= Seq(
        |  "http://imaginary-resolver.org/content/repositories/releases" at "Imaginary Resolver",
        |  "http://oss.sonatype.org/content/repositories/releases" at "Sonatype Releases"
        |)
        |
        |libraryDependencies ++= Seq(
        |  "org.scalatest" %% "scalatest" % "3.0.0" % "test" withSources(),
        |  "io.spray" %% "spray-json" % "1.3.2" withSources()
        |)
        |
        |javaOptions ++= Seq("-Xdebug", "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005")
        |
        |javaOptions in (Test, run) += "-Djava.awt.headless=true"
        |
        |jacoco.settings
      """.stripMargin.trim)
    fs.getFileContents("testproject/project/build.properties").trim should be ("""sbt.version=0.13.13""")
    fs.getFileContents("testproject/project/plugins.sbt").trim should be (
      """
        |addSbtPlugin("de.johoop" % "jacoco4sbt" % "2.2.0")
        |
        |addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.0")
      """.stripMargin.trim)
  }
  "more than one error in the user config is announced with a plural ('errors')" in {
    runWithUserConfig(
      """
        |{"refs": {
        |  "libraryRefs": {
        |    "testlib": [
        |      {
        |        "group":"com.github.madoc", "artifact":"testdep", "version":"0.1-SNAPSHOT",
        |        "impliedPlugins": ["non-existing-plugin"], "resolverRef": "non-existing-resolver"
        |      }
        |    ]
        |}}}
      """.stripMargin)("testproject")(noStandardOutput=true).getErrorOutput.trim should startWith("Errors in user configuration")
  }
  "whitespace in an element of a dependency results in an error message" in {
    runWithUserConfig(
      """
        |{"refs": {
        |  "libraryRefs": {
        |    "testlib": [{"group":"com.github.madoc ", "artifact":"testdep", "version":"0.1-SNAPSHOT"}]
        |}}}
      """.stripMargin)("testproject")(noStandardOutput=true).getErrorOutput.trim should be("""
        |Error in user configuration:
        |Whitespace after group ID of library definition: "com.github.madoc " %% "testdep" % "0.1-SNAPSHOT"
      """.stripMargin.trim)
  }
  "empty element of a dependency results in an error message" in {
    runWithUserConfig(
      """
        |{"refs": {
        |  "libraryRefs": {
        |    "testlib": [{"group":"", "artifact":"testdep", "version":"0.1-SNAPSHOT"}]
        |}}}
      """.stripMargin)("testproject")(noStandardOutput=true).getErrorOutput.trim should be("""
        |Error in user configuration:
        |Empty group ID of library definition: "" %% "testdep" % "0.1-SNAPSHOT"
      """.stripMargin.trim)
  }
  "duplicate Java option context results in an error message" in {
    runWithUserConfig(
      """
        |{
        |  "project": {
        |    "extraJavaOptions": [
        |      {"contexts": ["Test", "run"], "options": ["A", "B"]},
        |      {"contexts": ["Test", "run"], "options": ["C", "D"]}
        |    ]
        |  }
        |}
      """.stripMargin)("testproject")(noStandardOutput=true).getErrorOutput.trim should be("""
        |Error in user configuration:
        |Duplicate Java option context ("Test", "run").
      """.stripMargin.trim)
  }
  "using a plugin with implied SBT commands will add those commands to the build.sbt" in {
    val fs = runWithUserConfig(
      """
        |{"refs": {
        |  "pluginRefs": {
        |    "testplugin": [
        |      {
        |        "group":"com.github.madoc", "artifact":"testplugin", "version":"0.1-SNAPSHOT",
        |        "extraBuildCommands": ["A()", "B()"]
        |      }
        |    ]
        |}}}
      """.stripMargin)("-p", "testplugin", "testproject")(noStandardOutput=true, noErrorOutput=true)
    fs.getFileContents("testproject/build.sbt").trim should be (
      """
        |name := "testproject"
        |
        |A()
        |
        |B()
      """.stripMargin.trim)
  }
  "using a library that implies a plugin which in turn implies SBT commands will add those commands to the build.sbt" in {
    val fs = runWithUserConfig(
      """
        |{"refs": {
        |  "pluginRefs": {
        |    "testplugin": [
        |      {
        |        "group":"com.github.madoc", "artifact":"testplugin", "version":"0.1-SNAPSHOT",
        |        "extraBuildCommands": ["A()", "B()"]
        |      }
        |    ]
        |  },
        |  "libraryRefs": {
        |    "testlib": [
        |      {
        |        "group":"com.github.madoc", "artifact":"testdep", "version":"0.1-SNAPSHOT",
        |        "impliedPlugins": ["testplugin"]
        |      }
        |    ]
        |  }
        |}}
      """.stripMargin)("-l", "testlib", "testproject")(noStandardOutput=true, noErrorOutput=true)
    fs.getFileContents("testproject/build.sbt").trim should be (
      """
        |name := "testproject"
        |
        |libraryDependencies += "com.github.madoc" %% "testdep" % "0.1-SNAPSHOT"
        |
        |A()
        |
        |B()
      """.stripMargin.trim)
  }
}
