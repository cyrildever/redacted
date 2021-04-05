package fr.edgewhere.feistel.common.utils.xor

import fr.edgewhere.BasicUnitSpecs

/**
 * XORSpecs test class
 */
class XORSpecs extends BasicUnitSpecs {
  import fr.edgewhere.feistel.common.utils.xor.Operation._

  "the ^ operator" should "apply the XOR operation to strings" in {
    val xor1 = "a" ^ "b"
    xor1 should equal("\u0003")

    val xor2 = "ab" ^ "cd"
    xor2 should equal("\u0002\u0006")
  }
  it should "return the same value if applied twice" in {
    val d1 = "test"
    val keys = "keys"
    val xor3 = d1 ^ keys
    val d2 = xor3 ^ keys
    d2 should equal (d1)
  }
  "Neutral" should "keep the string unchanged when used for XOR" in {
    val expected = "Edgewhere"
    val neutral = Neutral(expected.length)
    val found = expected ^ neutral.toString
    found should equal (expected)
  }
}
