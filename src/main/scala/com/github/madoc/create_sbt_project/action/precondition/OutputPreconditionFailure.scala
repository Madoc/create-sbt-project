package com.github.madoc.create_sbt_project.action.precondition

import com.github.madoc.create_sbt_project.action.precondition.PreconditionFailure.{DirectoryDoesNotExist, DirectoryNotWritable, FileIsOnDirectoryPath}
import com.github.madoc.create_sbt_project.io.{Output, Write}

object OutputPreconditionFailure extends Output[PreconditionFailure] {
  def apply(the:PreconditionFailure)(write:Write) = the match {
    case DirectoryDoesNotExist(path) ⇒ write("Directory ").inQuotes(_ stringEscaped path)(" does not exist.")
    case DirectoryNotWritable(path) ⇒ write("Directory ").inQuotes(_ stringEscaped path)(" is not writable.")
    case FileIsOnDirectoryPath(path) ⇒
      write("A file exists on path ").inQuotes(_ stringEscaped path)(", but a directory is supposed to be there.")
  }

  val sequence:Output[Seq[PreconditionFailure]] = new Output[Seq[PreconditionFailure]] {
    def apply(the:Seq[PreconditionFailure])(write:Write) = the.foreach(write(_) lineBreak())
  }
}
