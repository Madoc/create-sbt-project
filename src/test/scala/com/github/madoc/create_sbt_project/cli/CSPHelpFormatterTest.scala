package com.github.madoc.create_sbt_project.cli

import java.io.PrintWriter

import com.github.madoc.create_sbt_project.io.Write.WriteToAppendable
import org.scalatest.{FreeSpec, Matchers}

class CSPHelpFormatterTest extends FreeSpec with Matchers {
  "directory is only attached to the usage line" in {
    val formatter = new CSPHelpFormatter
    val (sb1, sb2) = (new java.lang.StringBuilder, new java.lang.StringBuilder)
    formatter.printWrapped(new PrintWriter(new WriteToAppendable(sb1) asJavaIOWriter), 100, 2, "usage: a b c")
    formatter.printWrapped(new PrintWriter(new WriteToAppendable(sb2) asJavaIOWriter), 100, 2, "non-usage: a b c")
    sb1.toString.trim should be ("usage: a b c [directory]")
    sb2.toString.trim should be ("non-usage: a b c")
  }
}
