package pl.msitko.teleport

import cats.effect.IO
import os.Path

object Storage {
  // TODO: wrap in IO
  def read(location: Path): IO[TeleportState]               = ???
  def write(location: Path, state: TeleportState): IO[Unit] = ???
}
