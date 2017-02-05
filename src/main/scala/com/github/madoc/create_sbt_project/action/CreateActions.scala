package com.github.madoc.create_sbt_project.action

import java.nio.charset.Charset

import com.github.madoc.create_sbt_project.action.framework.{Action, ActionList}
import com.github.madoc.create_sbt_project.io.Output
import com.github.madoc.create_sbt_project.model.RootModel

object CreateActions extends (RootModel⇒ActionList)
{def apply(rootModel:RootModel):ActionList = new CreateActions(rootModel) actions}
private class CreateActions(rootModel:RootModel) {
  def actions:ActionList =
    `create main directory` >>
    `write build.sbt` >>
    `write .gitignore` >>
    `write build.properties` >>
    `write plugins.sbt`

  private val charset = Charset forName "utf-8"

  private def `create main directory`:Action = CreateDirectory(rootModel projectDirectory)
  private def `write build.sbt`:Action = outputToFile("build.sbt", buildDefinition)
  private def `write .gitignore`:Action = if(gitIgnore nonEmpty) outputToFile(".gitignore", gitIgnore) else ActionList.empty
  private def `write build.properties`:Action =
    if(buildProperties nonEmpty) CreateDirectory(path("project"))>>outputToFile("project/build.properties", buildProperties)
    else ActionList.empty
  private def `write plugins.sbt`:Action =
    if(pluginsDefinition nonEmpty) CreateDirectory(path("project"))>>outputToFile("project/plugins.sbt", pluginsDefinition)
    else ActionList.empty

  private def outputToFile[A](file:String, contents:A)(implicit output:Output[A]):Action =
    OutputToFile(path(file), contents, charset)
  private def path(p:String):String = {
    assert(p nonEmpty)
    rootModel.projectDirectory match {
      case "" ⇒ p
      case endsWithSlash if endsWithSlash endsWith "/" ⇒ endsWithSlash + p
      case doesNotEndWithSlash ⇒ doesNotEndWithSlash + "/" + p
    }
  }

  private def buildDefinition = rootModel buildDefinition
  private def buildProperties = rootModel buildProperties
  private def gitIgnore = rootModel gitIgnore
  private def pluginsDefinition = rootModel pluginsDefinition
}
