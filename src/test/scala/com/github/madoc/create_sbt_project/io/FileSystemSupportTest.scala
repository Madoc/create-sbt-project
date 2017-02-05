package com.github.madoc.create_sbt_project.io

import java.nio.file.FileSystems

import org.scalatest.{FreeSpec, Matchers}

class FileSystemSupportTest extends FreeSpec with Matchers {
  "user home directory returns the system property" in {
    val fs = new FileSystemSupport.Default(FileSystems getDefault) {
      def exposeUserHomeDirectory = userHomeDirectory
    }
    fs.exposeUserHomeDirectory should be (System getProperty "user.home")
  }
  "path of user preferences path is composed correctly even when user home ends in a slash" in {
    val fs = new FileSystemSupport.Default(FileSystems getDefault) {
      def exposeUserPreferencesFilePath = super.userPreferencesFilePath
      override protected def userHomeDirectory = "/home/user/"
    }
    fs.exposeUserPreferencesFilePath should be ("/home/user/.create-sbt-project.json")
  }
}
