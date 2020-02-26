package pl.msitko.teleport

import cats.effect.ExitCase.Canceled
import cats.effect._
import cats.syntax.all._
import com.monovore.decline._

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    val allSubCommands = Opts
      .subcommand(Commands.add)
      .orElse(Opts.subcommand(Commands.list))
      .orElse(Opts.subcommand(Commands.remove))
      .orElse(Opts.subcommand(Commands.goto))
      .orElse(Opts.subcommand(Commands.version))

    val command: Command[CmdOptions] = Command(name = "", header = "")(allSubCommands)

    val res = command.parse(args) match {
      case Right(cmd: AddCmdOptions)    =>
        Handler.add(cmd).map {
          case Right(tpPoint) =>
            println("Creating teleport point:")
            println(tpPoint.fansi)
            ExitCode.Success
          case Left(err) =>
            println(err.fansi)
            ExitCode.Error
        }

      case Right(cmd @ ListCmdOptions)        =>
        Handler.list(cmd).map { tpPoints =>
          println("teleport points: " + fansi.Color.LightBlue(s"(total ${tpPoints.size})"))
          tpPoints.map(_.fansi).foreach(println)
          IO(ExitCode.Success)
        }

      case Right(cmd: RemoveCmdOptions) =>
        Handler.remove(cmd).map {
          case Right(tpPoint) =>
            println(s"removed teleport point [${fansi.Color.LightBlue(cmd.name)}]")
          case Left(err) =>
            println(err.fansi)
            ExitCode.Error
        }

      case Right(cmd: GotoCmdOptions)   =>
        Handler.goto(cmd).map {
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

    val program = IO(println(fansi.Color.LightBlue("Welcome!"))) *> res

    program.guaranteeCase {
      case Canceled =>
        IO(println("Interrupted: releasing and exiting!"))
      case _ =>
        IO(println("Normal exit!"))
    }
  }
}
