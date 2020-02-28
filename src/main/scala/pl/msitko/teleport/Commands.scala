package pl.msitko.teleport

import cats.syntax.all._
import com.monovore.decline.{Command, Opts}

sealed trait CmdOptions extends Product with Serializable

// folderPath - which type it should be of?
final case class AddCmdOptions(name: String, folderPath: Option[String]) extends CmdOptions
final case object ListCmdOptions                                         extends CmdOptions
final case class RemoveCmdOptions(name: String)                          extends CmdOptions
final case class GotoCmdOptions(name: String)                            extends CmdOptions
final case object VersionCmdOptions                                      extends CmdOptions

final case class GlobalFlags(colors: Boolean, headers: Boolean)

object Commands {

  val flags = {
    val nocolorsOpt  = booleanFlag("no-colors", help = "Disable ANSI color codes")
    val noheadersOpt = booleanFlag("no-headers", help = "Disable printing headers for tabular data")
    (nocolorsOpt, noheadersOpt).mapN((noColors, noHeaders) => GlobalFlags(!noColors, !noHeaders))
  }

  val nameOpt = Opts.argument[String]("NAME")

  val add =
    Command(
      name = "add",
      header = "add a teleport point"
    ) {
      (nameOpt, Opts.argument[String]("FOLDERPATH").orNone).mapN(AddCmdOptions)
    }

  val list =
    Command(
      name = "list",
      header = "list all teleport points"
    ) {
      Opts.unit.map(_ => ListCmdOptions)
    }

  val remove =
    Command(
      name = "remove",
      header = "remove a teleport point"
    ) {
      nameOpt.map(RemoveCmdOptions)
    }

  val goto =
    Command(
      name = "goto",
      header = "go to a created teleport point"
    ) {
      nameOpt.map(GotoCmdOptions)
    }

  val version =
    Command(
      name = "version",
      header = "display version"
    ) {
      Opts.unit.map(_ => VersionCmdOptions)
    }

  val subcommands = Opts
    .subcommand(Commands.add)
    .orElse(Opts.subcommand(Commands.list))
    .orElse(Opts.subcommand(Commands.remove))
    .orElse(Opts.subcommand(Commands.goto))
    .orElse(Opts.subcommand(Commands.version))

  val allSubCommands: Opts[(GlobalFlags, CmdOptions)] = (flags, subcommands).tupled

  private def booleanFlag(long: String, help: String): Opts[Boolean] =
    Opts.flag(long = long, help = help).map(_ => true).withDefault(false)

}
