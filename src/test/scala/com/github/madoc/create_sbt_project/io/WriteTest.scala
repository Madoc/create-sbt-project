package com.github.madoc.create_sbt_project.io

import com.github.madoc.create_sbt_project.io.Write.WriteToAppendable
import org.scalatest.{FreeSpec, Matchers}

class WriteTest extends FreeSpec with Matchers {
  "writing a single character works as expected" in {
    val sb = new java.lang.StringBuilder
    val write = new Write {
      def apply(str:String) = {sb append str; this}
    }
    write('X')
    sb.toString should be ("X")
  }
  "the java.io.Writer for a Write instance ignores calls to flush() and close()" in {
    val sb = new java.lang.StringBuilder
    val w = new WriteToAppendable(sb)
    val writer = w.asJavaIOWriter
    writer.append("x").close()
    writer.close()
    writer.flush()
    writer.append("y")
    sb.toString should be ("xy")
  }
  "string escaping works as expected" in {
    val sb = new java.lang.StringBuilder
    new WriteToAppendable(sb).stringEscaped("line\nbreak, carriage\rreturn \t tab slash \\ quote \"")
    sb.toString should be ("line\\nbreak, carriage\\rreturn \\t tab slash \\\\ quote \\\"")
  }
}
