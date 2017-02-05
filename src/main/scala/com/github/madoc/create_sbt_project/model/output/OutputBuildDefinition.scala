package com.github.madoc.create_sbt_project.model.output

import com.github.madoc.create_sbt_project.io.{Output, Write}
import com.github.madoc.create_sbt_project.model.BuildDefinition

object OutputBuildDefinition extends Output[BuildDefinition] {
  def apply(the:BuildDefinition)(_write:Write) {
    val write = _write.withLinesSeparatedByDoubleLineBreaks
    def optKeyAssignment(name:String, value:Option[String]) =
      value foreach {v ⇒ (write(name)(" := ") inQuotes {_ stringEscaped v}) lineBreak()}
    def optKeyAssignments(kvs:(String,Option[String])*) {kvs foreach {case(name,value)⇒optKeyAssignment(name,value)}}
    def addSeveralStringsToKey(name:String, values:Seq[String], keyAffix:String="") = if(values nonEmpty) {
      write(name); if(keyAffix nonEmpty) write(" ")(keyAffix)
      if(values.size==1) write(" += ") inQuotes {_ stringEscaped values.head}
      else {
        write(" ++= Seq(")
        var addComma = false
        values foreach {v ⇒ if(addComma) write(", ") else addComma=true; write inQuotes {_ stringEscaped v}}
        write(')')
      }
      write lineBreak()
    }
    def addSeveralToKey[U](name:String, values:Seq[Write⇒U]) = if(values nonEmpty) {
      if(values.size==1) {values.head(write(name)(" += ")); write lineBreak()}
      else {
        write(name)(" ++= Seq(")
        var addComma = false
        for(value←values) {
          if(addComma) write(',') else addComma=true
          _write.lineBreak()
          value(write("  "))
        }
        _write.lineBreak()
        write(')').lineBreak()
      }
    }

    optKeyAssignments("organization"→(the organization), "name"→(the name), "version"→(the version),
      "scalaVersion"→(the scalaVersion))
    addSeveralStringsToKey("scalacOptions", the extraScalacOptions)
    addSeveralToKey("resolvers", (the resolverURLsToNames).toSeq.sortBy(_ _2) map {case (url,resName)⇒
      w:Write ⇒ (w inQuotes {_ stringEscaped resName})(" at ") inQuotes {_ stringEscaped url}
    })
    addSeveralToKey("libraryDependencies", (the libraryDependencies) map {dep ⇒ w:Write ⇒ w(dep)})
    (the javaOptionContextsToOptions).toSeq.sortBy(_._1 toString) foreach {case(contexts,options)⇒
      if(contexts isEmpty) addSeveralStringsToKey("javaOptions", options)
      else addSeveralStringsToKey("javaOptions", options, keyAffix=s"in (${contexts.toSeq.sorted.mkString(", ")})")
    }
    (the additionalCommands) foreach {com ⇒ write(com) lineBreak()}
  }
}
