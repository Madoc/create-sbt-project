package com.github.madoc.create_sbt_project.cli

import java.io.PrintWriter

import org.apache.commons.cli.HelpFormatter

/** Specific `HelpFormatter` subclass for this project. Adds the "[directory]" to the usage line. */
class CSPHelpFormatter extends HelpFormatter {
  override def printWrapped(pw:PrintWriter, width:Int, nextLineTabStop:Int, text:String) =
    if(text startsWith "usage: ") super.printWrapped(pw, width, nextLineTabStop, text + " [directory]")
    else super.printWrapped(pw, width, nextLineTabStop, text)
}
