package com.github.madoc.create_sbt_project.model.output

import java.io.OutputStream

import com.github.madoc.create_sbt_project.io.{Output, Write}
import com.github.madoc.create_sbt_project.model.BuildProperties

object OutputBuildProperties extends Output[BuildProperties] {
  def apply(the:BuildProperties)(write:Write) {
    val p = new java.util.Properties
    (the sbtVersion) foreach {p setProperty("sbt.version", _)}
    val wr = write
    p.store(new OutputStream {
      var firstLineSkipped = false
      def write(b:Int) {
        if(firstLineSkipped) wr(b toChar) else if(b.toChar == '\n') firstLineSkipped=true
      }
    }, null)
  }
}
