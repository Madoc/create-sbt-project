package com.github.madoc.create_sbt_project.main

import com.github.madoc.create_sbt_project.io.TestFileSystemSupport
import org.scalatest.Matchers

trait MainTestTools {
  this:Matchers⇒

  def runWithNoUserConfig(args:String*)(noStandardOutput:Boolean=false, noErrorOutput:Boolean=false):TestFileSystemSupport =
    runWithOptionalUserConfig(None)(args:_*)(noStandardOutput, noErrorOutput)

  def runWithUserConfig(userConfig:String)(args:String*)(noStandardOutput:Boolean=false, noErrorOutput:Boolean=false):TestFileSystemSupport =
    runWithOptionalUserConfig(Some(userConfig))(args:_*)(noStandardOutput, noErrorOutput)

  private def runWithOptionalUserConfig(userConfig:Option[String])(args:String*)(noStandardOutput:Boolean=false, noErrorOutput:Boolean=false):TestFileSystemSupport = {
    val fs = new TestFileSystemSupport()
    userConfig foreach {ucfg⇒fs.setUserPreferencesFileContents(ucfg)}
    new Main(fs, args)
    checkOutputsEmpty(fs)(noStandardOutput, noErrorOutput)
    fs
  }

  private def checkOutputsEmpty(fs:TestFileSystemSupport)(standardOutputShouldBeEmpty:Boolean, standardErrorShouldBeEmpty:Boolean) {
    if(standardOutputShouldBeEmpty) fs.getStandardOutput should be ('empty)
    if(standardErrorShouldBeEmpty) fs.getErrorOutput should be ('empty)
  }

  def mockSystemProperty[A](key:String, value:String)(f: ⇒A):A = {
    val previousValue = System.getProperty(key)
    if(value == null) System.getProperties remove key else System setProperty (key, value)
    try f
    finally if(previousValue == null) System.getProperties remove key else  System setProperty (key, previousValue)
  }
  def mockSystemProperties[A](kv:(String,String)*)(f: ⇒A):A = kv match {
    case Seq() ⇒ f
    case Seq(head, tail@_*) ⇒ mockSystemProperty(head _1, head _2)(mockSystemProperties(tail:_*)(f))
  }
}
