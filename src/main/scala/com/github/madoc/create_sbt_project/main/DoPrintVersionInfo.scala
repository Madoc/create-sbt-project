package com.github.madoc.create_sbt_project.main

import com.github.madoc.create_sbt_project.io.FileSystemSupport
import com.github.madoc.create_sbt_project.version.BuildInfo

private object DoPrintVersionInfo extends (FileSystemSupport⇒MainProcessLogic.Result) {
  def apply(fs:FileSystemSupport) = {
    fs.writeToStandardOutput line s"create-sbt-project ${BuildInfo version}"
    Option(System getProperty "java.runtime.name") foreach {jrv⇒
      fs.writeToStandardOutput(jrv)
      Option(System getProperty "java.runtime.version") foreach {bn⇒
        fs.writeToStandardOutput(" (build ")(bn)(")")
      }
      fs.writeToStandardOutput.lineBreak()
    }
    Option(System getProperty "java.vm.name") foreach {jvn⇒
      fs.writeToStandardOutput(jvn)
      Option(System getProperty "java.vm.version") foreach {jvv⇒
        fs.writeToStandardOutput(" (build ")(jvv)
        Option(System getProperty "java.vm.info") foreach {jvi⇒
          fs.writeToStandardOutput(", ")(jvi)
        }
        fs.writeToStandardOutput(")")
      }
      fs.writeToStandardOutput.lineBreak()
    }
    MainProcessLogic.Success
  }
}
