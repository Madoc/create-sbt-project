package com.github.madoc.create_sbt_project.config.validation

import com.github.madoc.create_sbt_project.config.LibraryRefConfig
import com.github.madoc.create_sbt_project.config.validation.ConfigError.ErrorLocation.{ConfigProperty, LibraryRefConfigLocation, ReferredBy}
import com.github.madoc.create_sbt_project.config.validation.ConfigError.ErrorNature.{ElementIsEmpty, MissingDefinition}
import com.github.madoc.create_sbt_project.config.validation.ConfigError.SomethingToDefine
import com.github.madoc.create_sbt_project.io.Write.WriteToAppendable
import org.scalatest.{FreeSpec, Matchers}

class OutputConfigErrorTest extends FreeSpec with Matchers {
  "library dependency config are written correctly even for the non-typical case" in {
    val lib = LibraryRefConfig("group", "artifact", "revision",
      addScalaVersion=Some(false), configuration=Some("test"), withSources=Some(true))
    val error = ConfigError(
      MissingDefinition(SomethingToDefine Plugin, "dummy-plugin"),
      ReferredBy(LibraryRefConfigLocation(lib)))
    outputAsString(error) should be ("""Missing definition for plugin "dummy-plugin", as referred to in library definition: "group" % "artifact" % "revision" % "test" withSources()""")
  }
  "various config properties are correctly reported as error locations" in {
    outputAsString(ElementIsEmpty asErrorAt ConfigProperty.ProjectDirectory) should be ("""Empty project directory.""")
    outputAsString(ElementIsEmpty asErrorAt ConfigProperty.ProjectName) should be ("""Empty project name.""")
    outputAsString(ElementIsEmpty asErrorAt ConfigProperty.ProjectOrganization) should be ("""Empty project organization.""")
    outputAsString(ElementIsEmpty asErrorAt ConfigProperty.ProjectVersion) should be ("""Empty project version.""")
    outputAsString(ElementIsEmpty asErrorAt ConfigProperty.SBTVersion) should be ("""Empty SBT version.""")
    outputAsString(ElementIsEmpty asErrorAt ConfigProperty.ScalaVersion) should be ("""Empty Scala version.""")
  }

  private def outputAsString(error:ConfigError):String = {
    val sb = new java.lang.StringBuilder
    OutputConfigError(error)(new WriteToAppendable(sb))
    sb.toString trim
  }
}
