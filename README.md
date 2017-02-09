create-sbt-project
==================

[![Build Status](https://travis-ci.org/Madoc/create-sbt-project.svg?branch=master)](https://travis-ci.org/Madoc/create-sbt-project)
[![Coverage Status](https://coveralls.io/repos/github/Madoc/create-sbt-project/badge.svg?branch=master)](https://coveralls.io/github/Madoc/create-sbt-project?branch=master)

A tool that creates new SBT projects for you, to speed up the time it takes until you can start coding.
Intended to be used from the command-line.

**Latest release version:** `0.1.0`, Git tag: `release-0.1.0`<br/>
**Current master:** `0.1.1-SNAPSHOT`

### Basic scope

Here are some things that `create-sbt-project` can do for you:
* create the basic directory structure of your  new SBT project
* add those common files: `build.sbt`, `.gitignore`, `project/build.properties` and `project/plugins.sbt`
* fill those files with your preferred contents, including libraries and plugins
* read your preferences from a configuration file in your home directory, so you do not always have to supply them
again
* add certain SBT build commands for certain plugins, for example `jacoco.settings` in case the JaCoCo plugin is
used

### Out of scope

In contrast, these are things that it _cannot_ do:
* It cannot create a `Build.scala` file.
* It cannot check if the library versions that you are using are actually the lastest ones available on the internet.
* It cannot change an _already existing_ SBT project. With this tool, you can only create a new project.

### Planned features

This is a list of things that `create-sbt-project` cannot _yet_ do, but that are planned for the future:
* do a `git init` in the newly created project directory, and commit the initial version
* execute `sbt compile` once in the new project folder, in order to verify that all plugins and dependencies can
actually be retrieved.

### Installation as shell command

Being a Scala program, `create-sbt-project` compiles into a JAR file.
This is not directly exectuable in your shell.
In order to make it executable, you have to follow steps like these:

* Clone this Git repository to your local machine, at a convenient location.
  * Make sure that you clone the latest release version, which you can find at the Git tag named at the top of this document, unless you want to use a snapshot version.
* Execute `sbt assembly`.
* Copy the resulting file `target/scala-2.11/create-sbt-project-assembly-*.jar` to a convenient location for future
execution. (The `*` in the mentioned path denotes the project version.)
* _Either_:
  * Create a shell script like this:
  
```
#!/bin/sh
java -jar <JAR-location> $@
```

  * Replace `<JAR-location>` with the absolute path of the previously created JAR file.
  * It is recommended to call this shell script `create-sbt-project`.
  * Make sure the script is in a directory that is contained in your `$PATH`.
  * Do not forget to make it exectuable, as in `chmod +x create-sbt-project` for Unix-like systems.
* _Or_:
  * Change your `.bashrc` or `.zshrc` to add an appropriate alias that does the same as the above mentioned shell
script.
* Those techniques work for Unix-like systems, such as the various Linuxes or macOS. For Windows systems, the
creation of a `.bat` file might be advisable, but the author of this document does not know about this.
* A final method could be to use [Scala-Native](http://www.scala-native.org/) to create a native executable for
`create-sbt-project`. This could be beneficiary because of the shorter startup time, but the author of this
document has not yet tried this.

### Usage

Most simple use case:

```
create-sbt-project my-new-project
```

When run without a configuration while in the user's home directory, this will simply create a new folder called
`my-new-project`, containing just one `build.sbt` file, and the empty folder `src/main/scala`.
The build file will only contain the line: `name := "my-new-project"`

The parameter `my-new-project` is the name of the _project directory_.
Since no separate _project name_ has been supplied, this is inferred from the project directory, which means that the
SBT project name will in this case be the same as the project directory name.

(This also works the other way round: When only a project name is supplied, but no directory, then the directory will
be assumed to be the same as the project name.)

In order to separate these two settings, one could have called the tool like this:

```
create-sbt-project --name my-new-project project-directory
```

In this case, the SBT project name in the `build.sbt` would be `my-new-project`, but the project would be created in
the new folder `project-directory`.

Apart from that, there are many, many more configuration options.
In oder to see them all, type:

```
create-sbt-project --help
```

##### Library, plugin and resolver refs

In order to add libraries and plugins to the new project, use the `-l` (or `--library`) and `-p` (or `--plugin`)
options, such as for example:

```
create-sbt-project -n my-akka-project -l akka -p scalatest
```

This will create a project named `my-akka-project` that contains a library named `akka` and a plugin named
`scalatest`.
`create-sbt-project` does _not_, however, know what `akka` and `scalatest` mean.
Those have to be defined in your user configuration file.
So, calling the above line without any user configuration file would only produce error output:

```
> create-sbt-project -n my-akka-project -l akka -p scalatest
Missing definition for library "akka".
Missing definition for plugin "scalatest".
```

Since those are only aliases for a library and a plugin, those are called "library ref" and "plugin ref" here.
What is missing is called a "library definition" and a "plugin definition", for those refs, respectively.

The same goes for resolvers: They are also referred to by alias, or _resolver ref_, and expected to be defined
elsewhere.

You define library, plugin and resolver refs in a file that resides in your _user configuration file_.

##### User configuration file

The user configuration file resides in the current user's home directory and is called `.create-sbt-project.json`.
As the name suggests, it is a JSON file.

Here are some examples for what you can configure in this file:
* a default project version, in case no version is explicitly set on the command line (e.g. `0.1-SNAPSHOT`)
* a default project organization
* Scala and SBT versions
* extra options to pass on to `scalac` and `java`, for various SBT execution contexts
* contents of the `.gitignore` file
* library and plugin refs to add to new projects by default
* definitions for your preferred library, plugin and resolver refs

A complete description of the JSON configuration format is still pending, at the time of writing this documentation.
However, `create-sbt-project` comes shipped with an example configuration, which you can load by supplying the
command-line option `-i` or `--example-config`.

Also, it can output its current configuration in completeness, including any modifications due to command-line
arguments, when you supply the command line option `-V` or `--print-config`.
Supplying that option prevents `create-sbt-project` from creating a new SBT project.
It will just read the user configuration file, apply the other command-line arguments to it, and then print the
resulting configuration JSON and exit.

By combining those two arguments, you can do something like:

```
create-sbt-project -iV
```

This will print the example configuration to the standard output and exit.
You can use that example configuration as a basis to model your own, until a more detailed explaination of the
user configuration format is provided here.
