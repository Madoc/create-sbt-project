package com.github.madoc.create_sbt_project.io

import com.github.madoc.create_sbt_project.action.precondition.{OutputPreconditionFailure, PreconditionFailure}
import com.github.madoc.create_sbt_project.config.validation.{ConfigError, OutputConfigError}
import com.github.madoc.create_sbt_project.io.Write.WriteToAppendable
import com.github.madoc.create_sbt_project.model._
import com.github.madoc.create_sbt_project.model.output.{OutputBuildDefinition, OutputBuildProperties, OutputGitIgnore, OutputPluginsDefinition, _}

trait Output[-A] {
  def apply(the:A)(write:Write)
  def asString(the:A):String = {
    val sb = new java.lang.StringBuilder
    apply(the)(new WriteToAppendable(sb))
    sb.toString
  }
}
object Output {
  def asString[A](the:A)(implicit output:Output[A]):String = output asString the

  implicit val forBuildDefinition:Output[BuildDefinition] = OutputBuildDefinition
  implicit val forBuildProperties:Output[BuildProperties] = OutputBuildProperties
  implicit val forConfigError:Output[ConfigError] = OutputConfigError
  implicit val forConfigErrorSequence:Output[Seq[ConfigError]] = OutputConfigError sequence
  implicit val forGitIgnore:Output[GitIgnore] = OutputGitIgnore
  implicit val forLibraryDependency:Output[LibraryDependency] = OutputLibraryDependency
  implicit val forPluginsDefinition:Output[PluginsDefinition] = OutputPluginsDefinition
  implicit val forPreconditionFailure:Output[PreconditionFailure] = OutputPreconditionFailure
  implicit val forPreconditionFailureSequence:Output[Seq[PreconditionFailure]] = OutputPreconditionFailure sequence
}
