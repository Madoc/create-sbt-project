package com.github.madoc.create_sbt_project.main

import com.github.madoc.create_sbt_project.cli.TerminalInfo
import com.github.madoc.create_sbt_project.io.{FileSystemSupport, Write}

private object DoPrintHelp extends ((FileSystemSupport,(Int⇒Write⇒Any))⇒MainProcessLogic.Result) {
  def apply(fs:FileSystemSupport, writeWithWidth:Int⇒Write⇒Any) = {
    val terminalWidth = TerminalInfo width match {
      case oneOrLess if oneOrLess<2 ⇒ 74
      case tooLow if tooLow<20 ⇒ 20
      case plausibleValue ⇒ plausibleValue
    }
    writeWithWidth(terminalWidth)(fs writeToStandardOutput)
    MainProcessLogic.Success
  }
}
