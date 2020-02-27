package pl.msitko.teleport

import os.Path
import io.circe.generic.JsonCodec

@JsonCodec
final case class TeleportPoint(name: String, absFolderPath: Path) {
  def fansi: _root_.fansi.Str = s"$name\t" + _root_.fansi.Color.LightBlue("")
}

@JsonCodec
final case class TeleportState(points: List[TeleportPoint]) extends AnyVal {
  def prepend(t: TeleportPoint): TeleportState = copy(points = t :: points)
}
