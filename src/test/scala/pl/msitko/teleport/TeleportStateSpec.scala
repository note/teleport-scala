package pl.msitko.teleport

import cats.syntax.all._
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.wordspec.AnyWordSpec

class TeleportStateSpec extends AnyWordSpec with TypeCheckedTripleEquals {

  val initialState = TeleportState(
    List(
      TeleportPoint("a", os.Path("/home/a")),
      TeleportPoint("b", os.Path("/home/b")),
      TeleportPoint("c", os.Path("/home/c"))
    ))

  "afterRemoval" should {
    "return (Some, newState) in case name already existed in the state" in {
      val expected = TeleportState(
        List(
          TeleportPoint("a", os.Path("/home/a")),
          TeleportPoint("c", os.Path("/home/c"))
        ))

      assert(initialState.afterRemoval("b") === (TeleportPoint("b", os.Path("/home/b")).some, expected))
    }

    "return (None, previousState) in case name already existed in the state" in {
      assert(initialState.afterRemoval("nonexisting") === initialState)
    }
  }
}
