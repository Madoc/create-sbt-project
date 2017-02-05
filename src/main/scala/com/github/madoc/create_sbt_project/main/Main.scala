package com.github.madoc.create_sbt_project.main

import com.github.madoc.create_sbt_project.io.FileSystemSupport

class Main(fs:FileSystemSupport, commandLineArguments:Seq[String]) {
  new MainProcessLogic(fs, commandLineArguments) execute()
}
object Main extends App {
  locally {new Main(FileSystemSupport.main get, args)}
}
