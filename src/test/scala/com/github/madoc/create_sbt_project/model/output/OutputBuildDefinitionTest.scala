package com.github.madoc.create_sbt_project.model.output

import com.github.madoc.create_sbt_project.io.Write.WriteToAppendable
import com.github.madoc.create_sbt_project.model.BuildDefinition
import org.scalatest.{FreeSpec, Matchers}

class OutputBuildDefinitionTest extends FreeSpec with Matchers {
  "writing a build definition with just one resolver will add a '+=' instead of '++='" in {
    val sb = new java.lang.StringBuilder
    OutputBuildDefinition(BuildDefinition(resolverURLsToNames = Map("Single Resolver"→"http://illegal-url")))(new WriteToAppendable(sb))
    sb.toString.trim should be ("""resolvers += "http://illegal-url" at "Single Resolver"""")
  }
}
