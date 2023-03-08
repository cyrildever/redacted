package com.cyrildever.redacted.core

import com.cyrildever.BasicUnitSpecs
import com.cyrildever.feistel.Feistel
import com.cyrildever.feistel.common.utils.hash.Engine._
import com.cyrildever.redacted.model.Dictionary

/**
 * RedactorSpecs test class
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 2.0
 */
class RedactorSpecs extends BasicUnitSpecs {
  "redact" should "be deterministic" in {
    val dic = Dictionary(List("M.", "Cyril", "Antoine", "Laurent", "Dever"))
    val expected = "B6ds. is testing ¼= Du:,l26 library while ¾.=y£|v Izizb is listening to Âvhis*l<"

    val txt = "Cyril is testing M. Dever's library while Antoine Dever is listening to Laurent."
    val redactor = Redactor(dic, Feistel.FPECipher(SHA_256, "1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef", 10))
    var redacted = redactor.redact(txt)
    redacted should equal (expected)

    val expanded = redactor.expand(redacted)
    expanded should equal (txt)

    val blake2 = "¸lk€$ is testing F: B!@x7;1 library while Cs>v0'* ¹'90< is listening to Pz2;ws?o"
    redacted = Redactor(dic, Feistel.FPECipher(BLAKE2b, "1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef", 10)).redact(txt)
    redacted should equal (blake2)

    val keccak = "H1i,{ is testing ½5 ¿&bv8f8 library while ¸&7+r$u ¹|6'h is listening to Å€j;$\"4<"
    redacted = Redactor(dic, Feistel.FPECipher(KECCAK, "1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef", 10)).redact(txt)
    redacted should equal (keccak)
  }
}
