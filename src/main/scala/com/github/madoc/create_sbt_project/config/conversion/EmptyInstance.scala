package com.github.madoc.create_sbt_project.config.conversion

import com.github.madoc.create_sbt_project.config.{ProjectConfig, RefsConfig, RootConfig}

case class EmptyInstance[+A](empty:A)
object EmptyInstance {
  implicit val emptyProjectConfig:EmptyInstance[ProjectConfig] = EmptyInstance(ProjectConfig())
  implicit val emptyRefsConfig:EmptyInstance[RefsConfig] = EmptyInstance(RefsConfig())
  implicit val emptyRootConfig:EmptyInstance[RootConfig] = EmptyInstance(RootConfig())

  implicit def emptyMap[A,B]:EmptyInstance[Map[A,B]] = EmptyMap.asInstanceOf[EmptyInstance[Map[A,B]]]
  implicit def emptySet[A]:EmptyInstance[Set[A]] = EmptySet.asInstanceOf[EmptyInstance[Set[A]]]
  implicit def emptySeq[A]:EmptyInstance[Seq[A]] = EmptySeq.asInstanceOf[EmptyInstance[Seq[A]]]

  private object EmptyMap extends EmptyInstance[Map[Any,Any]](Map empty)
  private object EmptySet extends EmptyInstance[Set[Any]](Set empty)
  private object EmptySeq extends EmptyInstance[Seq[Any]](Seq empty)
}
