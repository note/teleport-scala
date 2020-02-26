package pl.msitko.teleport

import cats.effect.IO
import os.Path

import scala.util.Try

final case class TeleportPoint(name: String, absFolderPath: String) {
  def fansi: fansi.Str = s"$name\t" + _root_.fansi.Color.LightBlue("")
}

object Backend {
  def read(location: Path): Either[]
}

sealed trait TeleportError extends Product with Serializable {
  def fansi: fansi.Str = ???
}

object Handler {
  def add(cmd: AddCmdOptions): IO[Either[TeleportError, TeleportPoint]] = ???

  // ordering - the most recent one first
  def list(cmd: ListCmdOptions.type): IO[List[TeleportPoint]] = ???

  def remove(cmd: RemoveCmdOptions): IO[Either[TeleportError, ()]] = ???

  def goto(gotoCmdOptions: GotoCmdOptions): IO[Either[TeleportError, os.Path]] = ???
}
