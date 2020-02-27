package pl.msitko.teleport

import cats.effect.IO
import os.Path
import cats.syntax.all._
import fansi.Str

sealed trait TeleportError extends Product with Serializable {
  def fansi: _root_.fansi.Str
}

final case class DirectoryDoesNotExists(path: Path) extends TeleportError {
  override def fansi: Str = _root_.fansi.Color.Red(s"User error: directory $path does not exists")
}

final case class IsFile(path: Path) extends TeleportError {

  override def fansi: Str =
    _root_.fansi.Color.Red(s"User error: $path is a file but teleport expects is to be a directory")
}

final case class TeleportPointNotFound(name: String) extends TeleportError {
  override def fansi: Str = _root_.fansi.Color.Red(s"User error: teleport point $name does not exist")
}

class Handler(storage: Storage) {

  def add(cmd: AddCmdOptions): IO[Either[TeleportError, TeleportPoint]] = {
    val path = cmd.folderPath.fold(os.pwd)(os.pwd / _)
    if (os.exists(path)) if (os.isDir(path)) {
      for {
        state <- storage.read()
        newPoint = TeleportPoint(cmd.name, path)
        newState = state.prepend(newPoint)
        _ <- storage.write(newState)
      } yield newPoint.asRight
    } else {
      IsFile(path).asLeft.pure[IO]
    }
    else {
      DirectoryDoesNotExists(path).asLeft.pure[IO]
    }
  }

  // TODO: define ordering
  def list(): IO[TeleportState] = storage.read()

  def remove(cmd: RemoveCmdOptions): IO[Either[TeleportError, Unit]] =
    for {
      currentState <- storage.read()
      (toBeRemoved, updated) = currentState.points.partition(_.name == cmd.name)
      res <- if (toBeRemoved.isEmpty) {
        TeleportPointNotFound(cmd.name).asLeft.pure[IO]
      } else {
        storage.write(TeleportState(updated)) *> ().asRight.pure[IO]
      }
    } yield res

  def goto(cmd: GotoCmdOptions): IO[Either[TeleportError, os.Path]] = {
    storage.read().map { state =>
      state.points.find(_.name == cmd.name) match {
        case Some(p) =>
          // We may ensure whether the directory still exists
          p.absFolderPath.asRight
        case None => TeleportPointNotFound(cmd.name).asLeft
      }
    }
  }
}
