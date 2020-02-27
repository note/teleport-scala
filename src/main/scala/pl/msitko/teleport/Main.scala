package pl.msitko.teleport

import cats.effect.ExitCase.Canceled
import cats.effect._
import cats.syntax.all._
import com.monovore.decline._

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    val command: Command[CmdOptions] = Command(name = "", header = "")(Commands.allSubCommands)
    val storage                      = new Storage(os.pwd / ".teleport-data")
    val handler                      = new Handler(storage)

    val program = command.parse(args) match {
      case Right(cmd: AddCmdOptions) =>
        handler.add(cmd).map {
          case Right(tpPoint) =>
            println("Creating teleport point:")
            println(tpPoint.fansi)
            ExitCode.Success
          case Left(err) =>
            println(err.fansi)
            ExitCode.Error
        }

      case Right(ListCmdOptions) =>
        handler.list().map { state =>
          println("teleport points: " + fansi.Color.LightBlue(s"(total ${state.points.size})"))
          state.points.map(_.fansi).foreach(println)
          ExitCode.Success
        }

      case Right(cmd: RemoveCmdOptions) =>
        handler.remove(cmd).map {
          case Right(_) =>
            println(s"removed teleport point [${fansi.Color.LightBlue(cmd.name)}]")
            ExitCode.Success
          case Left(err) =>
            println(err.fansi)
            ExitCode.Error
        }

      case Right(cmd: GotoCmdOptions) =>
        handler.goto(cmd).map {
          case Right(absolutePath) =>
            println(absolutePath)
            ExitCode(2)
          case Left(err) =>
            println(err.fansi)
            ExitCode.Error
        }

      case Right(VersionCmdOptions) =>
        IO(println(BuildInfo.version)) *> IO(ExitCode.Success)

      case Left(e) => IO(println(s"error: ${e.toString}")) *> IO(ExitCode.Error)
    }

    // if program has any resource they can be released here:
    program.guaranteeCase {
      case Canceled =>
        IO.unit
      case _ =>
        IO.unit
    }
  }
}
