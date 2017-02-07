package com.github.madoc.create_sbt_project.main

import com.github.madoc.create_sbt_project.cli.TerminalInfo
import com.github.madoc.create_sbt_project.io.{FileSystemSupport, Write}

private object DoPrintHelp extends ((FileSystemSupport,(Int⇒Write⇒Any))⇒MainProcessLogic.Result) {
  def apply(fs:FileSystemSupport, writeWithWidth:Int⇒Write⇒Any) = {
    writeWithWidth(terminalWidth)(fs writeToStandardOutput)
    MainProcessLogic.Success
  }

  private def terminalWidth:Int = plausibleTerminalWidth(TerminalInfo width)

  private[main] def plausibleTerminalWidth(givenTerminalWidth:Int):Int = givenTerminalWidth match {
    case oneOrLess if oneOrLess<2 ⇒ 74
    case tooLow if tooLow<20 ⇒ 20
    case plausibleValue ⇒ plausibleValue
  }
}
