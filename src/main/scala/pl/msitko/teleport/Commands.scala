package pl.msitko.teleport

import java.nio.file.Path

import com.monovore.decline.{Command, Opts}
import cats.syntax.all._

sealed trait CmdOptions extends Product with Serializable

// folderPath - which type it should be of?
final case class AddCmdOptions(name: String, folderPath: Option[String]) extends CmdOptions
final case object ListCmdOptions                                         extends CmdOptions
final case class RemoveCmdOptions(name: String)                          extends CmdOptions
final case class GotoCmdOptions(name: String)                            extends CmdOptions
final case object VersionCmdOptions                                      extends CmdOptions

object Commands {
  val nameOpt = Opts.argument[String]("NAME")

  // TODO: add no-color option?

  val add =
    Command(
      name = "add",
      header = "add a teleport point"
    ) {
      // TODO: should we use refined for NonEmptyString?
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
}
