organization := "com.github.madoc"

name := "create-sbt-project"

version := "0.1.0"

scalaVersion := "2.11.8"

scalacOptions ++= Seq("-language:_", "-unchecked", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq(
  "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases",
  "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots"
)

libraryDependencies ++= Seq(
  "commons-cli" % "commons-cli" % "1.3.1" withSources(),
  "com.google.jimfs" % "jimfs" % "1.1" % "test" withSources(),
  "io.spray" %%  "spray-json" % "1.3.2" withSources(),
  "org.jline" % "jline" % "3.1.2" withSources(),
  "org.scalatest" %% "scalatest" % "3.0.0" % "test" withSources(),
  "org.scalacheck" %% "scalacheck" % "1.13.3" % "test" withSources(),
  "org.scoverage" %% "scalac-scoverage-runtime" % "1.3.0" % "test" withSources()
)

javaOptions ++= Seq("-Xdebug", "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005")

javaOptions in (Test, run) += "-Djava.awt.headless=true"

jacoco.settings

lazy val root = (project in file(".")).
  enablePlugins(BuildInfoPlugin).
  settings(
    buildInfoKeys := Seq[BuildInfoKey](version),
    buildInfoPackage := "com.github.madoc.create_sbt_project.version"
  )
