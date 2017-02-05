package com.github.madoc.create_sbt_project.config

object ExampleConfiguration {
  lazy val root = RootConfig(Some(project), Some(refs))

  lazy val project = ProjectConfig(
    organization = Some("com.mycompany"),
    version = Some("0.1-SNAPSHOT"),
    scalaVersion = Some("2.12.1"),
    sbtVersion = Some("0.13.13"),
    extraScalacOptions = Some(Seq("-unchecked", "-deprecation", "-encoding", "utf8")),
    extraJavaOptions = Some(Set(
      JavaContextOptionConfig(options = Seq("-Xdebug", "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005")),
      JavaContextOptionConfig(contexts = Some(Set("Test", "run")), options = Seq("-Djava.awt.headless=true"))
    )),
    gitIgnore = Some(Set(
      ".idea",
      ".idea_modules",
      ".c9",
      "*.iml",
      "target",
      "out",
      "project/target",
      "project/project",
      "project.vim",
      "*~",
      "*#",
      ".DS_Store",
      "src/autogenerated"
    ))
  )

  lazy val refs = RefsConfig(
    libraryRefs=Some(libraryRefs),
    pluginRefs=Some(pluginRefs),
    resolverRefs=Some(resolverRefs)
  )

  lazy val libraryRefs:Map[String,Set[LibraryRefConfig]] = Map(
    "akka" → Set(LibraryRefConfig(
      "com.typesafe.akka", "akka-actor", "2.4.16", withSources=Some(true)
    )),
    "scala-xml" → Set(LibraryRefConfig(
      "org.scala-lang.modules", "scala-xml", "1.0.6", withSources=Some(true)
    )),
    "scalatest" → Set(LibraryRefConfig(
      "org.scalatest", "scalatest", "3.0.0", configuration=Some("test"), withSources=Some(true)
    )),
    "scalacheck" → Set(LibraryRefConfig(
      "org.scalacheck", "scalacheck", "1.13.3", configuration=Some("test"), withSources=Some(true)
    ))
  )

  lazy val pluginRefs:Map[String,Set[PluginRefConfig]] = Map(
    "jacoco" → Set(PluginRefConfig(
      "de.johoop", "jacoco4sbt", "2.2.0", extraBuildCommands=Some(Seq("jacoco.settings"))
    )),
    "scoverage" → Set(PluginRefConfig(
      "org.scoverage", "sbt-scoverage", "1.5.0"
    ))
  )

  lazy val resolverRefs:Map[String,Set[ResolverRefConfig]] = Map(
    "sonatype-rel" → Set(ResolverRefConfig("Sonatype Releases", "http://oss.sonatype.org/content/repositories/releases")),
    "sonatype-snap" → Set(ResolverRefConfig("Sonatype Snapshots", "http://oss.sonatype.org/content/repositories/snapshots"))
  )
}
