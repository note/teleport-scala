package example

import java.nio.file.Path

import cats.effect.ExitCase.Canceled
import cats.effect._
import cats.syntax.all._
import com.monovore.decline._

import scala.concurrent.duration._

sealed trait CmdOptions extends Product with Serializable

final case class AddCmdOptions(name: String, folderPath: Option[Path]) extends CmdOptions
final case object ListCmdOptions                                       extends CmdOptions
final case class RemoveCmdOptions(name: String)                        extends CmdOptions
final case class GotoCmdOptions(name: String)                          extends CmdOptions

object Commands {
  val nameOpt = Opts.argument[String]("NAME")

  // TODO: add no-color option?

  val add = Command(
    name = "add",
    header = "add a teleport point"
  ) {
    // TODO: should we use refined for NonEmptyString?
    (nameOpt, Opts.argument[Path]("FOLDERPATH").orNone).mapN(AddCmdOptions)
  }

  val list = Command(
    name = "list",
    header = "list all teleport points"
  ) {
    Opts.unit.map(_ => ListCmdOptions)
  }

  val remove = Command(
    name = "remove",
    header = "remove a teleport point"
  ) {
    nameOpt.map(RemoveCmdOptions)
  }

  val goto = Command(
    name = "goto",
    header = "go to a created teleport point"
  ) {
    nameOpt.map(GotoCmdOptions)
  }
}

object Main extends IOApp {

  val all = Opts
    .subcommand(Commands.add)
    .orElse(Opts.subcommand(Commands.list))
    .orElse(Opts.subcommand(Commands.remove))
    .orElse(Opts.subcommand(Commands.goto))

  def run(args: List[String]): IO[ExitCode] = {
    val command: Command[CmdOptions] = Command(name = "", header = "")(all)

    val res = command.parse(args) match {
      case Right(_) => IO(println("OK")) *> IO(ExitCode.Success)
      case Left(e)  => IO(println(s"error: $e")) *> IO(ExitCode.Error)
    }

    val program = IO(println(fansi.Color.LightBlue("Welcome!"))) *> res

    program.guaranteeCase {
      case Canceled =>
        IO(println("Interrupted: releasing and exiting!"))
      case _ =>
        IO(println("Normal exit!"))
    }
  }
}
