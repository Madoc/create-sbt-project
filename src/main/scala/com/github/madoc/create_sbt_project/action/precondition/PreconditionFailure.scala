package com.github.madoc.create_sbt_project.action.precondition

sealed trait PreconditionFailure
object PreconditionFailure {
  sealed case class DirectoryDoesNotExist(path:String) extends PreconditionFailure
  sealed case class DirectoryNotWritable(path:String) extends PreconditionFailure
  sealed case class FileIsOnDirectoryPath(path:String) extends PreconditionFailure
}
