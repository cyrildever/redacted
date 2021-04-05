package fr.edgewhere.feistel

import fr.edgewhere.BasicUnitSpecs
import fr.edgewhere.feistel.common.utils.base256.Readable
import fr.edgewhere.feistel.common.utils.base256.Readable._
import fr.edgewhere.feistel.common.utils.hash.Engine._

/**
 * FeistelSpecs test class
 */
class FeistelSpecs extends BasicUnitSpecs {
  "FPECipher.encrypt" should "be deterministic" in {
    import fr.edgewhere.feistel.common.utils.base256.Readable._

    val expected = "K¡(#q|r5*"
    val cipher = Feistel.FPECipher(SHA_256, "8ed9dcc1701c064f0fd7ae235f15143f989920e0ee9658bb7882c8d7d5f05692", 10)
    val found = cipher.encrypt("Edgewhere")
    found.length should equal ("Edgewhere".length)
    found should equal (expected)
    found.toHex should equal ("2a5d07024f5a501409")

    val blake2 = Feistel.FPECipher(BLAKE2b, "8ed9dcc1701c064f0fd7ae235f15143f989920e0ee9658bb7882c8d7d5f05692", 10).encrypt("Edgewhere")
    blake2 should not equal (found)
    blake2 should equal ("¼u*$q0up¢")
  }
  "FPECipher.decrypt" should "be deterministic" in {
    val nonFPE = Readable("\u0002Edgewhere")
    val cipher = Feistel.FPECipher(SHA_256, "8ed9dcc1701c064f0fd7ae235f15143f989920e0ee9658bb7882c8d7d5f05692", 10)
    var found = cipher.decrypt(fromHex("3d7c0a0f51415a521054"))
    found should equal (nonFPE)

    val ref = "K¡(#q|r5*"
    val expected = "Edgewhere"
    found = cipher.decrypt(fromHex("2a5d07024f5a501409"))
    found should equal (expected)
    found = cipher.decrypt(ref)
    found should equal (expected)
    val b256 = fromHex("2a5d07024f5a501409")
    b256 should equal (ref)

    val fromBlake2 = Readable("¼u*$q0up¢")
    val blake2 = Feistel.FPECipher(BLAKE2b, "8ed9dcc1701c064f0fd7ae235f15143f989920e0ee9658bb7882c8d7d5f05692", 10).decrypt(fromBlake2)
    blake2 should equal (expected)
  }
}
