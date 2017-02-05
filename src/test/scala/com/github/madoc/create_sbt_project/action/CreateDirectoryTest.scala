package com.github.madoc.create_sbt_project.action

import com.github.madoc.create_sbt_project.action.precondition.PreconditionFailure.DirectoryNotWritable
import com.github.madoc.create_sbt_project.io.TestFileSystemSupport
import org.scalatest.{FreeSpec, Matchers}

class CreateDirectoryTest extends FreeSpec with Matchers {
  "CreateDirectory cannot deal with other failures than DirectoryDoesNotExist" in {
    CreateDirectory("dummy").mightDealWith(DirectoryNotWritable("dummy")) should be (false)
  }
  "trying to create a directory inside a non-writable directory returns an error" in {
    CreateDirectory("nonwritable/subdir").precondition(new TestFileSystemSupport() {
      override def fileExists(path:String) = path=="nonwritable"
      override def isDirectory(path:String) = path=="nonwritable"
      override def isFileWritable(path:String) = path!="nonwritable"
    }) should be (Seq(DirectoryNotWritable("nonwritable")))
  }
}
