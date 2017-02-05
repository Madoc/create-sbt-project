package com.github.madoc.create_sbt_project.config.validation

import com.github.madoc.create_sbt_project.config._
import com.github.madoc.create_sbt_project.config.validation.ConfigError.ErrorLocation.{ElementOf, LibraryRefConfigLocation, PluginRefConfigLocation, ReferredBy}
import com.github.madoc.create_sbt_project.config.validation.ConfigError.ErrorNature._
import com.github.madoc.create_sbt_project.config.validation.ConfigError._

object Validate {
  def apply(the:RootConfig):Seq[ConfigError] = Validate(the.project?, the.refs?) ++ Validate(the.refs?)

  private def apply(the:LibraryRefConfig):Seq[ConfigError] = whitespaceCheck(Left(the))

  private def whitespaceCheck(dependency:Either[LibraryRefConfig,PluginRefConfig]):Seq[ConfigError] =
    DependencyElement.all flatMap {element ⇒ whitespaceCheck(element of dependency, element, dependency)}

  private def whitespaceCheck(value:String, element:DependencyElement, dependency:Either[LibraryRefConfig,PluginRefConfig]):Seq[ConfigError] =
    whitespaceCheck(value, ElementOf(element, toErrorLocation(dependency)))

  private def whitespaceCheck(value:String, location:ErrorLocation):Seq[ConfigError] =
    if(value isEmpty) Seq(ElementIsEmpty asErrorAt location)
    else
      ((value(0) isWhitespace) leadsTo (WhitespaceBefore asErrorAt location)) ++
      ((value.last isWhitespace) leadsTo (WhitespaceAfter asErrorAt location))

  private def toErrorLocation(dependency:Either[LibraryRefConfig,PluginRefConfig]):ErrorLocation = dependency match {
    case Left(lib) ⇒ LibraryRefConfigLocation(lib)
    case Right(plugin) ⇒ PluginRefConfigLocation(plugin)
  }

  private def apply(the:LibraryRefConfig, refs:RefsConfig):Seq[ConfigError] =
    Validate(the) ++
    (refs lacksResolver the.resolverRef).leadsTo(
      MissingDefinition(SomethingToDefine Resolver, the.resolverRef get) asErrorAt ReferredBy(LibraryRefConfigLocation(the))
    ) ++
    the.impliedPlugins.?.flatMap(p ⇒ (refs lacksPlugin p) leadsTo (
      MissingDefinition(SomethingToDefine Plugin, p) asErrorAt ReferredBy(LibraryRefConfigLocation(the))
    ))

  private def apply(the:PluginRefConfig, refs:RefsConfig):Seq[ConfigError] =
    whitespaceCheck(Right(the)) ++
    ((refs lacksResolver the.resolverRef) leadsTo (
      MissingDefinition(SomethingToDefine Resolver, the.resolverRef get) asErrorAt ReferredBy(PluginRefConfigLocation(the)))
    )

  private def apply(the:ProjectConfig, refs:RefsConfig):Seq[ConfigError] =
    checkForDuplicateJavaOptionContexts(the) ++
    (ErrorLocation.ConfigProperty.all flatMap {prop ⇒ prop.of(the).toSeq flatMap {value ⇒ whitespaceCheck(value, prop)}}) ++
    ((the.directory isEmpty) && (the.name isEmpty)).leadsTo(NeitherNameNorDirectoryIsSet error) ++
    the.libraries.errorsWhen(refs lacksLibrary, {ref⇒MissingDefinition(SomethingToDefine Library, ref) error}) ++
    the.plugins.errorsWhen(refs lacksPlugin, {ref⇒MissingDefinition(SomethingToDefine Plugin, ref) error})

  private def checkForDuplicateJavaOptionContexts(project:ProjectConfig):Seq[ConfigError] =
    ((project.extraJavaOptions?).toSeq.map(_.contexts?).groupBy(identity) collect {
      case (context, Seq(_,_,_*)) ⇒ ConfigError(DuplicateJavaOptionContext(context))
    }).toSeq

  private def apply(refs:RefsConfig):Seq[ConfigError] =
    refs.libraryRefs.?.values.flatten.flatMap(Validate(_, refs)).toSeq ++
    refs.pluginRefs.?.values.flatten.flatMap(Validate(_, refs)).toSeq ++
    refs.resolverRefs.?.values.flatten.flatMap(Validate(_)).toSeq

  private def apply(the:ResolverRefConfig):Seq[ConfigError] = Seq()
}
