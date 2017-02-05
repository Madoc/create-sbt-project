package com.github.madoc.create_sbt_project.io

import java.nio.charset.Charset
import java.nio.file.{FileSystem, FileSystems, Files}
import java.util.concurrent.atomic.AtomicReference

import com.github.madoc.create_sbt_project.action.framework.ActionEnvironment
import com.github.madoc.create_sbt_project.io.Write.WriteToAppendable

import scala.io.Source

trait FileSystemSupport extends ActionEnvironment {
  def userPreferencesFileContents:Option[String]
  def writeToStandardOutput:Write
  def writeToErrorOutput:Write
}
object FileSystemSupport {
  val default:FileSystemSupport = new Default(FileSystems getDefault)
  val main:AtomicReference[FileSystemSupport] = new AtomicReference[FileSystemSupport](default)

  class Default(val fs:FileSystem) extends FileSystemSupport {
    def userPreferencesFileContents = {
      val prefsPath = fs getPath userPreferencesFilePath
      if(Files.exists(prefsPath) && Files.isRegularFile(prefsPath) && Files.isReadable(prefsPath)) {
        val source = Source.fromInputStream(Files newInputStream prefsPath, "utf-8")
        try Some(source mkString) finally source.close()
      }
      else None
    }

    val writeToErrorOutput = new WriteToAppendable(systemErr)
    val writeToStandardOutput = new WriteToAppendable(systemOut)
    def fileExists(path:String) = Files exists (fs getPath path)
    def isFileWritable(path:String) = Files isWritable (fs getPath path)
    def isDirectory(path:String) = Files isDirectory (fs getPath path)
    def mkdirs(path:String) = Files createDirectories (fs getPath path)
    def outputToFile[A](contents:A, path:String, charset:Charset)(implicit output:Output[A]) = {
      val writer = Files.newBufferedWriter(fs getPath path, charset)
      try output(contents)(new WriteToAppendable(writer)) finally writer close
    }

    protected def systemErr:Appendable = System err
    protected def systemOut:Appendable = System out
    protected def userHomeDirectory:String = System.getProperty("user.home")
    protected def userPreferencesFilePath:String =
      if(userHomeDirectory endsWith "/") userHomeDirectory + ".create-sbt-project.json"
      else userHomeDirectory + "/.create-sbt-project.json"
  }
}
