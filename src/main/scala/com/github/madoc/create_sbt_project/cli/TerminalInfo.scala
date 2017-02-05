package com.github.madoc.create_sbt_project.cli

import java.util.logging.{Level, Logger}

import org.jline.terminal.TerminalBuilder

object TerminalInfo {
  lazy val width:Int = {
    Logger.getLogger("org.jline").setLevel(Level.SEVERE)
    TerminalBuilder.terminal().getWidth
  }
}
