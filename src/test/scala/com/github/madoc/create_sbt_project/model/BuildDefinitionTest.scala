package com.github.madoc.create_sbt_project.model

import org.scalatest.{FreeSpec, Matchers}

class BuildDefinitionTest extends FreeSpec with Matchers {
  "basic test" in {
    val buildFile = BuildDefinition(
      name = Some("create-sbt-project"),
      organization = Some("eu.madoc"),
      version = Some("0.1-SNAPSHOT"),
      scalaVersion = Some("2.11.8"),
      extraScalacOptions = Seq("-language:_", "-unchecked", "-deprecation", "-encoding", "utf8"),
      resolverURLsToNames = Map(
        "http://oss.sonatype.org/content/repositories/snapshots" → "Sonatype Snapshots",
        "http://oss.sonatype.org/content/repositories/releases" → "Sonatype Releases"
      ),
      libraryDependencies = Seq(
        LibraryDependency(groupID="org.scalatest", artifactID="scalatest", revision="3.0.0", configuration=Some("test"), withSources=true),
        LibraryDependency(groupID="org.scalacheck", artifactID="scalacheck", revision="1.13.3", configuration=Some("test"), withSources=true)
      ),
      javaOptionContextsToOptions = Map(
        Set[String]() → Seq("-Xdebug", "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"),
        Set("Test","run") → Seq("-Djava.awt.headless=true")
      ),
      additionalCommands=Seq("jacoco.settings")
    )
    buildFile.toString should be (
      """organization := "eu.madoc"
        |
        |name := "create-sbt-project"
        |
        |version := "0.1-SNAPSHOT"
        |
        |scalaVersion := "2.11.8"
        |
        |scalacOptions ++= Seq("-language:_", "-unchecked", "-deprecation", "-encoding", "utf8")
        |
        |resolvers ++= Seq(
        |  "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases",
        |  "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots"
        |)
        |
        |libraryDependencies ++= Seq(
        |  "org.scalatest" %% "scalatest" % "3.0.0" % "test" withSources(),
        |  "org.scalacheck" %% "scalacheck" % "1.13.3" % "test" withSources()
        |)
        |
        |javaOptions ++= Seq("-Xdebug", "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005")
        |
        |javaOptions in (Test, run) += "-Djava.awt.headless=true"
        |
        |jacoco.settings
        |""" stripMargin
    )
  }
}
