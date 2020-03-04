package pl.msitko.teleport

import cats.syntax.all._
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.wordspec.AnyWordSpec

class TeleportStateSpec extends AnyWordSpec with TypeCheckedTripleEquals {

  val initialState = TeleportState(
    List(
      TeleportPoint("a", os.pwd / "a"),
      TeleportPoint("b", os.pwd / "b"),
      TeleportPoint("c", os.pwd / "c")
    ))

  "afterRemoval" should {
    "return (Some, newState) in case name already existed in the state" in {
      val expected = TeleportState(
        List(
          TeleportPoint("a", os.pwd / "a"),
          TeleportPoint("c", os.pwd / "c")
        ))

      assert(initialState.afterRemoval("b") === (TeleportPoint("b", os.pwd / "b").some -> expected))
    }

    "return (None, previousState) in case name already existed in the state" in {
      assert(initialState.afterRemoval("nonexisting") === (None -> initialState))
    }
  }
}
