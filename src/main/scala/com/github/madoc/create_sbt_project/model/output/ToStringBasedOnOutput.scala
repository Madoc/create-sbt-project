package com.github.madoc.create_sbt_project.model.output

import com.github.madoc.create_sbt_project.io.Output

abstract class ToStringBasedOnOutput[A:Output] {this:A ⇒ override def toString = Output asString this}
