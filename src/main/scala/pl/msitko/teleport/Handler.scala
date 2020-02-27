package pl.msitko.teleport

import cats.effect.IO
import os.Path
import cats.syntax.all._

sealed trait TeleportError extends Product with Serializable {
  def fansi: _root_.fansi.Str = ???
}

final case class DirectoryDoesNotExists(path: Path) extends TeleportError
final case class IsFile(path: Path)                 extends TeleportError

object Handler {
  val location: os.Path = ???

  def add(cmd: AddCmdOptions)(implicit s: Storage.type): IO[Either[TeleportError, TeleportPoint]] = {
    val path = cmd.folderPath.fold(os.pwd)(os.pwd / _)
    if (os.exists(path)) {
      if (os.isDir(path)) {
        for {
          state <- s.read(location)
          newPoint = TeleportPoint(cmd.name, path)
          newState = state.append(newPoint)
          _ <- s.write(location, newState)
        } yield newPoint.asRight
      } else {
        IsFile(path).asLeft.pure[IO]
      }
    } else {
      DirectoryDoesNotExists(path).asLeft.pure[IO]
    }
  }

  // TODO: define ordering
  def list(cmd: ListCmdOptions.type): IO[TeleportState] = ???

  def remove(cmd: RemoveCmdOptions): IO[Either[TeleportError, Unit]] = ???

  def goto(gotoCmdOptions: GotoCmdOptions): IO[Either[TeleportError, os.Path]] = ???
}
