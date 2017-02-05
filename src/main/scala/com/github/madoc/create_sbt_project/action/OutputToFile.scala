package com.github.madoc.create_sbt_project.action

import java.io.File
import java.nio.charset.Charset

import com.github.madoc.create_sbt_project.action.framework.{Action, ActionEnvironment, ActionResult}
import com.github.madoc.create_sbt_project.action.precondition.PreconditionFailure
import com.github.madoc.create_sbt_project.action.precondition.PreconditionFailure.{DirectoryDoesNotExist, DirectoryNotWritable, FileIsOnDirectoryPath}
import com.github.madoc.create_sbt_project.io.Output

case class OutputToFile[A](path:String, contents:A, charset:Charset)(implicit output:Output[A]) extends Action {
  protected def run(env:ActionEnvironment) = {env outputToFile (contents, path, charset); ActionResult.Success}
  def precondition = {env:ActionEnvironment ⇒
    val parent = Option(new File(path) getParent) getOrElse "."
    if(!(env fileExists parent)) Seq(DirectoryDoesNotExist(parent))
    else if(!(env isFileWritable parent)) Seq(DirectoryNotWritable(parent))
    else if(!(env isDirectory parent)) Seq(FileIsOnDirectoryPath(parent))
    else Seq()
  }
  def mightDealWith(failure:PreconditionFailure) = false
}
