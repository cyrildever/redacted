package fr.edgewhere.feistel.common.utils.padding

import fr.edgewhere.BasicUnitSpecs
import fr.edgewhere.feistel.common.utils.padding.EvenPadder._

/**
 * EvenPadderSpecs test class
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 1.0
 */
class EvenPadderSpecs extends BasicUnitSpecs {
  "EvenPadder" should "pad and unpad a string correctly" in {
    val expected = "Edgewhere"
    val padded = pad(expected)
    (padded.length % 2) should equal (0)
    padded should equal ("\u0002Edgewhere")
    padded should startWith (LEFT_PAD_CHARACTER.toString)
    val found = unpad(padded)
    found should equal (expected)
  }
}
