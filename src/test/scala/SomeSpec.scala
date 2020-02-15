package example

import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.wordspec.AnyWordSpec

class SomeSpec extends AnyWordSpec with TypeCheckedTripleEquals {
	"2 + 2" should {
		"equal 4" in {
			assert((2 + 2) === 4)
		}
	}
}