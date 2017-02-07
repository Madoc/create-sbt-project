package com.github.madoc.create_sbt_project.main

import org.scalatest.{FreeSpec, Matchers}

class DoPrintHelpTest extends FreeSpec with Matchers {
  "terminal width for help text is corrected if implausible" in {
    DoPrintHelp.plausibleTerminalWidth(1) should be (74)
    DoPrintHelp.plausibleTerminalWidth(-1) should be (74)
    DoPrintHelp.plausibleTerminalWidth(15) should be (20)
    DoPrintHelp.plausibleTerminalWidth(120) should be (120)
  }
}
