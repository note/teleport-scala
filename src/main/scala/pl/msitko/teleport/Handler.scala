package pl.msitko.teleport

import cats.effect.IO
import os.Path
import cats.syntax.all._

import scala.util.Try

sealed trait TeleportError extends Product with Serializable {
  def fansi(implicit s: Style): _root_.fansi.Str = s.error(msg)
  def msg: String
}

final case class DirectoryDoesNotExists(path: Path) extends TeleportError {
  override def msg: String = s"User error: directory $path does not exists"
}

final case class IsFile(path: Path) extends TeleportError {
  override def msg: String = s"User error: $path is a file but teleport expects is to be a directory"
}

final case class TeleportPointAlreadyExists(name: String) extends TeleportError {
  override def msg: String = s"User error: teleport point [$name] already exists"
}

final case class TeleportPointNotFound(name: String) extends TeleportError {
  override def msg: String = s"User error: teleport point [$name] does not exist"
}

class Handler(storage: Storage) {

  def add(cmd: AddCmdOptions): IO[Either[TeleportError, TeleportPoint]] = {
    val absolutePath = cmd.folderPath.map(fp => Try(Path(fp)).getOrElse(os.pwd / fp))
    val path         = absolutePath.fold(os.pwd)(identity)
    if (os.exists(path)) if (os.isDir(path)) {
      for {
        state <- storage.read()
        newPoint    = TeleportPoint(cmd.name, path)
        newStateOpt = state.prepend(newPoint)
        res <- newStateOpt
          .map(newState => storage.write(newState) *> (newPoint).asRight.pure[IO])
          .getOrElse(TeleportPointAlreadyExists(cmd.name).asLeft.pure[IO])
      } yield res
    } else {
      IsFile(path).asLeft.pure[IO]
    }
    else {
      DirectoryDoesNotExists(path).asLeft.pure[IO]
    }
  }

  def list(): IO[TeleportState] = storage.read()

  def remove(cmd: RemoveCmdOptions): IO[Either[TeleportError, Unit]] =
    for {
      currentState <- storage.read()
      (toBeRemoved, updated) = currentState.afterRemoval(cmd.name)
      res <- toBeRemoved match {
        case Some(_) => storage.write(updated) *> ().asRight.pure[IO]
        case None    => TeleportPointNotFound(cmd.name).asLeft.pure[IO]
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
