package pl.msitko.teleport

import os.Path

final case class TeleportPoint(name: String, absFolderPath: Path) {
  def fansi: _root_.fansi.Str = s"$name\t" + _root_.fansi.Color.LightBlue("")
}

final case class TeleportState(points: List[TeleportPoint]) extends AnyVal {
  def append(t: TeleportPoint): TeleportState = ???
}
