package com.github.madoc.create_sbt_project.config.validation

import com.github.madoc.create_sbt_project.config.validation.ConfigError.{ErrorLocation, ErrorNature}
import com.github.madoc.create_sbt_project.config.{LibraryRefConfig, PluginRefConfig, ProjectConfig}

case class ConfigError(nature:ErrorNature, location:ErrorLocation=ErrorLocation Unspecific) {
  def mightBeSolvedByCommandLineArgs:Boolean = nature mightBeSolvedByCommandLineArgs
}
object ConfigError {
  implicit class BooleanSugar(condition:Boolean) {
    def leadsTo[A<:ConfigError](error: ⇒A):Seq[A] = if(condition) Seq(error) else Seq()
  }

  sealed trait ErrorNature {
    def error:ConfigError = ConfigError(this)
    def asErrorAt(location:ErrorLocation) = ConfigError(this, location)
    def mightBeSolvedByCommandLineArgs:Boolean = false
  }
  object ErrorNature {
    sealed case class DuplicateJavaOptionContext(context:Set[String]) extends ErrorNature
    object ElementIsEmpty extends ErrorNature
    sealed case class MissingDefinition(what:SomethingToDefine, ref:String) extends ErrorNature
    object MoreThanOneProjectDirectorySpeficied extends ErrorNature
    object NeitherNameNorDirectoryIsSet extends ErrorNature {
      override def mightBeSolvedByCommandLineArgs = true
    }
    object UnrecognizedCommandLineOption extends ErrorNature
    object WhitespaceAfter extends ErrorNature
    object WhitespaceBefore extends ErrorNature
  }

  sealed trait ErrorLocation
  object ErrorLocation {
    sealed case class CommandLineArgument(arg:String) extends ErrorLocation
    sealed case class ElementOf(element:DependencyElement, of:ErrorLocation) extends ErrorLocation
    sealed case class LibraryRefConfigLocation(libraryRefConfig:LibraryRefConfig) extends ErrorLocation
    sealed case class PluginRefConfigLocation(pluginRefConfig:PluginRefConfig) extends ErrorLocation
    sealed trait ConfigProperty extends ErrorLocation {
      def of(project:ProjectConfig):Option[String]
    }
    sealed case class ReferredBy(origin:ErrorLocation) extends ErrorLocation
    object Unspecific extends ErrorLocation

    object ConfigProperty {
      val all:Seq[ConfigProperty] = Seq(ProjectDirectory, ProjectName, ProjectOrganization, ProjectVersion, SBTVersion,
        ScalaVersion)
      object ProjectDirectory extends ConfigProperty {def of(the:ProjectConfig) = the directory}
      object ProjectName extends ConfigProperty {def of(the:ProjectConfig) = the name}
      object ProjectOrganization extends ConfigProperty {def of(the:ProjectConfig) = the organization}
      object ProjectVersion extends ConfigProperty {def of(the:ProjectConfig) = the version}
      object SBTVersion extends ConfigProperty {def of(the:ProjectConfig) = the sbtVersion}
      object ScalaVersion extends ConfigProperty {def of(the:ProjectConfig) = the scalaVersion}
    }
  }

  sealed trait SomethingToDefine
  object SomethingToDefine {
    object Library extends SomethingToDefine
    object Plugin extends SomethingToDefine
    object Resolver extends SomethingToDefine
  }

  sealed trait DependencyElement {
    def name:String
    def of(the:Either[LibraryRefConfig,PluginRefConfig]):String
  }
  object DependencyElement {
    val all:Seq[DependencyElement] = Seq(GroupID, ArtifactID, Revision)
    object GroupID extends DependencyElement {
      def name = "group ID"
      def of(the:Either[LibraryRefConfig, PluginRefConfig]) = the match {
        case Left(lib) ⇒ lib group
        case Right(plugin) ⇒ plugin group
      }
    }
    object ArtifactID extends DependencyElement {
      def name = "artifact ID"
      def of(the:Either[LibraryRefConfig, PluginRefConfig]) = the match {
        case Left(lib) ⇒ lib artifact
        case Right(plugin) ⇒ plugin artifact
      }
    }
    object Revision extends DependencyElement {
      def name = "revision"
      def of(the:Either[LibraryRefConfig, PluginRefConfig]) = the match {
        case Left(lib) ⇒ lib version
        case Right(plugin) ⇒ plugin version
      }
    }
  }
}
