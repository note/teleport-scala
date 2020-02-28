package pl.msitko.teleport

import cats.effect.ExitCase.Canceled
import cats.effect._
import cats.syntax.all._
import com.monovore.decline._

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {

    // TODO: add comment that global flags have to go before subcommand
    val command: Command[(GlobalFlags, CmdOptions)] = Command(
      name = "teleport-scala",
      header = "teleport: A tool to quickly switch between directories",
      helpFlag = true)(Commands.allSubCommands)
    val storage = new Storage(os.home / ".teleport-data")
    val handler = new Handler(storage)

    val program = command.parse(args) match {
      case Right((globalFlags, cmd)) =>
        val style = if (globalFlags.colors) {
          DefaultStyle
        } else {
          NoColorsStyle
        }
        dispatchCmd(globalFlags, cmd, handler)(style)

      case Left(e) =>
        IO(println(e.toString())) *> IO(ExitCode.Error)
    }

    // if program has any resource they can be released here:
    program.guaranteeCase {
      case Canceled =>
        IO.unit
      case _ =>
        IO.unit
    }
  }

  private def dispatchCmd(globalFlags: GlobalFlags, cmd: CmdOptions, handler: Handler)(
      implicit style: Style): IO[ExitCode] =
    cmd match {
      case cmd: AddCmdOptions =>
        handler.add(cmd).map {
          case Right(tpPoint) =>
            println(s"Creating teleport point: ${style.emphasis(tpPoint.name)}")
            ExitCode.Success
          case Left(err) =>
            println(err.fansi)
            ExitCode.Error
        }

      case ListCmdOptions =>
        handler.list().map { state =>
          if (globalFlags.headers) {
            println("teleport points: " + style.emphasis(s"(total ${state.points.size})"))
          }
          state.points.map(_.fansi).foreach(println)
          ExitCode.Success
        }

      case cmd: RemoveCmdOptions =>
        handler.remove(cmd).map {
          case Right(_) =>
            println(s"removed teleport point [${style.emphasis(cmd.name)}]")
            ExitCode.Success
          case Left(err) =>
            println(err.fansi)
            ExitCode.Error
        }

      case cmd: GotoCmdOptions =>
        handler.goto(cmd).map {
          case Right(absolutePath) =>
            println(absolutePath)
            ExitCode(2)
          case Left(err) =>
            println(err.fansi)
            ExitCode.Error
        }

      case VersionCmdOptions =>
        IO(println(BuildInfo.version)) *> IO(ExitCode.Success)
    }

}
