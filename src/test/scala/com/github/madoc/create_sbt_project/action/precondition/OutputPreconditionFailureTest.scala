package com.github.madoc.create_sbt_project.action.precondition

import com.github.madoc.create_sbt_project.action.precondition.PreconditionFailure.{DirectoryDoesNotExist, DirectoryNotWritable, FileIsOnDirectoryPath}
import com.github.madoc.create_sbt_project.io.Output
import org.scalatest.{FreeSpec, Matchers}

class OutputPreconditionFailureTest extends FreeSpec with Matchers {
  "a sequence of precondition failures is written to output correctly" in {
    val failures = Seq(
      DirectoryDoesNotExist("foobar"),
      DirectoryNotWritable("baz"),
      FileIsOnDirectoryPath("foobaz")
    )
    Output.asString(failures).trim should be (
      """
        |Directory "foobar" does not exist.
        |Directory "baz" is not writable.
        |A file exists on path "foobaz", but a directory is supposed to be there.
      """.stripMargin.trim)
  }
}
