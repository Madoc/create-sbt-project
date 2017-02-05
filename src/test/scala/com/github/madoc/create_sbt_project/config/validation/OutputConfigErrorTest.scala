package com.github.madoc.create_sbt_project.config.validation

import com.github.madoc.create_sbt_project.config.LibraryRefConfig
import com.github.madoc.create_sbt_project.config.validation.ConfigError.ErrorLocation.{LibraryRefConfigLocation, ReferredBy}
import com.github.madoc.create_sbt_project.config.validation.ConfigError.ErrorNature.MissingDefinition
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
    val sb = new java.lang.StringBuilder
    OutputConfigError(error)(new WriteToAppendable(sb))
    sb.toString.trim should be ("""Missing definition for plugin "dummy-plugin", as referred to in library definition: "group" % "artifact" % "revision" % "test" withSources()""")
  }
}
