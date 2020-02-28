package pl.msitko.teleport

import cats.effect.IO
import os.Path
import io.circe.parser.parse
import cats.syntax.all._
import io.circe.syntax._
import pl.msitko.teleport.TeleportState._

// TODO: move somewhere else:
@SuppressWarnings(Array("org.wartremover.warts.Null")) // integration with java interface (RuntimeException)
final case class TeleportException(msg: String, underlying: Option[Exception])
    extends RuntimeException(msg, underlying.orNull)

class Storage(location: Path) {

  def read(): IO[TeleportState] =
    IO(os.exists(location)).flatMap {
      case true =>
        IO(os.read(location)).flatMap { content =>
          IO.fromEither(parse(content).flatMap(parsed => parsed.as[TeleportState]).leftMap { e =>
            TeleportException(e.getMessage, e.some)
          })
        }
      case false =>
        TeleportState.empty.pure[IO]
    }

  def write(state: TeleportState): IO[Unit] = {
    val content = state.asJson.noSpaces
    IO(os.write.over(location, content))
  }
}
