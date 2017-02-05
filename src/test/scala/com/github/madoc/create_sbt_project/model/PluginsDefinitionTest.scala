package com.github.madoc.create_sbt_project.model

import org.scalatest.{FreeSpec, Matchers}

class PluginsDefinitionTest extends FreeSpec with Matchers {
  "basic test" in {
    val pd = PluginsDefinition(Set(LibraryDependency("de.johoop", "jacoco4sbt", "2.2.0", addScalaVersion=false)))
    pd.toString should be ("addSbtPlugin(\"de.johoop\" % \"jacoco4sbt\" % \"2.2.0\")\n")
  }
}
