package com.github.madoc.create_sbt_project.model

import org.scalatest.{FreeSpec, Matchers}

class LibraryDependencyTest extends FreeSpec with Matchers {
  "simple case" in {
    val ld=LibraryDependency(groupID="eu.madoc", artifactID="create_sbt_project", revision="0.1-SNAPSHOT")
    ld.toString should be (""""eu.madoc" %% "create_sbt_project" % "0.1-SNAPSHOT"""")
  }
  "without adding scala version" in {
    val ld=LibraryDependency(groupID="eu.madoc", artifactID="create_sbt_project", revision="0.1-SNAPSHOT", addScalaVersion=false)
    ld.toString should be (""""eu.madoc" % "create_sbt_project" % "0.1-SNAPSHOT"""")
  }
  "for 'test'" in {
    val ld=LibraryDependency(groupID="eu.madoc", artifactID="create_sbt_project", revision="0.1-SNAPSHOT", configuration=Some("test"))
    ld.toString should be (""""eu.madoc" %% "create_sbt_project" % "0.1-SNAPSHOT" % "test"""")
  }
  "with sources" in {
    val ld=LibraryDependency(groupID="eu.madoc", artifactID="create_sbt_project", revision="0.1-SNAPSHOT", withSources=true)
    ld.toString should be (""""eu.madoc" %% "create_sbt_project" % "0.1-SNAPSHOT" withSources()""")
  }
  "everything together" in {
    val ld=LibraryDependency(groupID="eu.madoc", artifactID="create_sbt_project", revision="0.1-SNAPSHOT", addScalaVersion=false, configuration=Some("test"), withSources=true)
    ld.toString should be (""""eu.madoc" % "create_sbt_project" % "0.1-SNAPSHOT" % "test" withSources()""")
  }
}
