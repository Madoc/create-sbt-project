package com.github.madoc.create_sbt_project.action

import java.io.File

import com.github.madoc.create_sbt_project.action.framework.{Action, ActionEnvironment, ActionResult}
import com.github.madoc.create_sbt_project.action.precondition.PreconditionFailure
import com.github.madoc.create_sbt_project.action.precondition.PreconditionFailure.{DirectoryDoesNotExist, DirectoryNotWritable, FileIsOnDirectoryPath}

case class CreateDirectory(path:String) extends Action {
  protected def run(env:ActionEnvironment) = {env mkdirs path; ActionResult.Success}
  def precondition = {env:ActionEnvironment ⇒
    var p = path
    while(!((env fileExists path) || new File(p).getParentFile==null)) p=new File(p).getParent
    if(!(env fileExists p)) Seq()
    else if(!(env isDirectory p)) Seq(FileIsOnDirectoryPath(p))
    else if(!(env isFileWritable p)) Seq(DirectoryNotWritable(p))
    else Seq()
  }
  def mightDealWith(failure:PreconditionFailure) = failure match {
    case DirectoryDoesNotExist(dir) ⇒ dir startsWith path
    case _ ⇒ false
  }
}
