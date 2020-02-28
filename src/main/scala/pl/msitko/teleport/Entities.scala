package pl.msitko.teleport

import io.circe.{Decoder, Encoder}
import os.Path
import io.circe.generic.JsonCodec

import scala.util.Try
import Codecs._

object Codecs {
  implicit val pathEncoder: Encoder[Path] = Encoder.encodeString.contramap(_.toString())
  implicit val pathDecoder: Decoder[Path] = Decoder.decodeString.emapTry(s => Try(Path(s)))
}

@JsonCodec
final case class TeleportPoint(name: String, absFolderPath: Path) {
  def fansi(implicit s: Style): _root_.fansi.Str = s"$name\t" + s.emphasis(absFolderPath.toString())
}

@JsonCodec
final case class TeleportState(points: List[TeleportPoint]) extends AnyVal {
  def prepend(t: TeleportPoint): TeleportState = copy(points = t :: points)
}

object TeleportState {
  def empty: TeleportState = TeleportState(points = List.empty)
}

//object Codecs {
//  implicit val teleportPointCodec = deriveCodec[TeleportPoint]
//  implicit val teleportStateCodec = deriveCodec[TeleportState]
//}
