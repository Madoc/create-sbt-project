package com.github.madoc.create_sbt_project

import com.github.madoc.create_sbt_project.config.conversion.EmptyInstance
import com.github.madoc.create_sbt_project.config.validation.ConfigError
import spray.json.DefaultJsonProtocol._

package object config {
  implicit class OptionSugar[A](option:Option[A]) {
    def ?(implicit e:EmptyInstance[A]):A = option getOrElse e.empty
  }
  implicit class SetOptionSugar[A](setOption:Option[Set[A]]) {
    def errorsWhen[B<:ConfigError](cond:A⇒Boolean, err:A⇒B):Seq[B] =
      setOption.?.toSeq.flatMap(a⇒if(cond(a)) Some(err(a)) else None)
  }

  implicit lazy val javaContextOptionConfigJsonFormat = jsonFormat2(JavaContextOptionConfig)
  implicit lazy val libraryRefConfigJsonFormat = jsonFormat8(LibraryRefConfig)
  implicit lazy val pluginRefJsonFormat = jsonFormat5(PluginRefConfig)
  implicit lazy val projectConfigJsonFormat = jsonFormat12(ProjectConfig)
  implicit lazy val resolverRefJsonFormat = jsonFormat2(ResolverRefConfig)
  implicit lazy val refsConfigJsonFormat = jsonFormat3(RefsConfig)
  implicit lazy val rootConfigJsonFormat = jsonFormat2(RootConfig)
}
