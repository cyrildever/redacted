package fr.edgewhere.feistel.common.utils.strings

import fr.edgewhere.BasicUnitSpecs

/**
 * StringsUtilSpecs test class
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 1.0
 */
class StringsUtilSpecs extends BasicUnitSpecs {
  import StringsUtil._

  "StringsUtil.add" should "be deterministic" in {
    val ref = "ÄÆ"
    val ab = "ab"
    val cd = "cd"
    val found = add(ab, cd)
    found should equal(ref)
  }
  "StringsUtil.extract" should "be deterministic" in {
    val ref = "s is a testThis is a tes"
    val found = extract("This is a test", 3, 24)
    found should equal(ref)
  }
  "StringsUtil.split" should "be deterministic" in {
    val left = "edge"
    val right = "where"
    val edgewhere = left + right
    val (l, r) = split(edgewhere)
    l should equal (left)
    r should equal (right)
  }
}
