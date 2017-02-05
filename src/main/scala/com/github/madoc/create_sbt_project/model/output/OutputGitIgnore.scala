package com.github.madoc.create_sbt_project.model.output

import com.github.madoc.create_sbt_project.io.{Output, Write}
import com.github.madoc.create_sbt_project.model.GitIgnore

object OutputGitIgnore extends Output[GitIgnore]
{def apply(the:GitIgnore)(write:Write) = (the entries).toSeq.sorted foreach {entry ⇒ write(entry).lineBreak()}}
