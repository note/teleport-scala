package pl.msitko.teleport

import fansi.Str

trait Style {
  def emphasis(input: String): fansi.Str
  def error(input: String): fansi.Str
}

object DefaultStyle extends Style {
  override def emphasis(input: String): Str = fansi.Color.LightBlue(input)

  override def error(input: String): Str = fansi.Color.Red(input)
}

object NoColorsStyle extends Style {
  override def emphasis(input: String): Str = Str(input)

  override def error(input: String): Str = Str(input)
}
