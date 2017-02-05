package com.github.madoc.create_sbt_project.io

import java.nio.charset.Charset
import java.nio.file.Files

import com.google.common.jimfs.{Configuration, Jimfs}

import scala.collection.JavaConversions

class TestFileSystemSupport(workingDirectory:String="/home/user/projectdir", userHome:String="/home/user")
extends FileSystemSupport.Default(Jimfs.newFileSystem(Configuration.unix().toBuilder.setWorkingDirectory(workingDirectory).build())) {
  private var errorOutput = ""
  private var standardOutput = ""
  private val lock = new Object

  locally {mkdirs(workingDirectory)}

  def getErrorOutput:String = lock synchronized errorOutput
  def getStandardOutput:String = lock synchronized standardOutput
  def setUserPreferencesFileContents(contents:String) =
    outputToFile(contents, userPreferencesFilePath, Charset forName "utf-8")(new Output[String] {
      def apply(the:String)(write:Write) = write(the)
    })
  def getFileContents(path:String, charset:Charset=Charset forName "utf-8"):String =
    JavaConversions.asScalaBuffer(Files.readAllLines(fs getPath path, charset)).mkString("\n")

  override protected def userHomeDirectory = userHome
  override protected def systemErr = new Appendable {
    def append(csq:CharSequence) = {lock synchronized {errorOutput += csq.toString}; this}
    def append(csq:CharSequence, start:Int, end:Int) = append(csq subSequence (start, end))
    def append(c:Char) = append(c toString)
  }
  override protected def systemOut = new Appendable {
    def append(csq:CharSequence) = {lock synchronized {standardOutput += csq.toString}; this}
    def append(csq:CharSequence, start:Int, end:Int) = append(csq subSequence (start, end))
    def append(c:Char) = append(c toString)
  }
}
