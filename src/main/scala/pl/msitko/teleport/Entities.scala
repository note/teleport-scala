package pl.msitko.teleport

import cats.syntax.all._
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

// `points` are ordered descending in addition order (i.e. newest first)
@JsonCodec
final case class TeleportState(points: List[TeleportPoint]) extends AnyVal {

  def prepend(t: TeleportPoint): Option[TeleportState] =
    points.find(_.name == t.name).fold(TeleportState(t :: points).some)(_ => None)

  def afterRemoval(name: String): (Option[TeleportPoint], TeleportState) = {
    val res = points.partition(_.name == name)
    (res._1.headOption, TeleportState(res._2))
  }
}

object TeleportState {
  def empty: TeleportState = TeleportState(points = List.empty)
}
