package com.github.madoc.create_sbt_project.action.framework

import java.nio.charset.Charset

import com.github.madoc.create_sbt_project.io.Output

trait ActionEnvironment {
  def fileExists(path:String):Boolean
  def isFileWritable(path:String):Boolean
  def isDirectory(path:String):Boolean
  def mkdirs(path:String)
  def outputToFile[A](contents:A, path:String, charset:Charset)(implicit output:Output[A])
}
