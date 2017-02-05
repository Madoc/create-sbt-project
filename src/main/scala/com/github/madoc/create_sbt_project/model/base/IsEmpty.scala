package com.github.madoc.create_sbt_project.model.base

trait IsEmpty {
  def isEmpty:Boolean
  def nonEmpty:Boolean = !isEmpty
}
