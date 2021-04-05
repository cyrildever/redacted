package fr.edgewhere.feistel.common.utils.hash

import fr.edgewhere.BasicUnitSpecs

/**
 * EngineSpecs test class
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 1.0
 */
class EngineSpecs extends BasicUnitSpecs {
  import Engine._
  import Hash._

  "Engine.hash" should "return the appropriate hash" in {
    val data = "Edgewhere"

    val blake2 = "e5ff44a9b2caa01099082dd6e9055ea5d002beea078e9251454494ccf6869b2f"
    var found = Engine.hash(data, BLAKE2b).toHex
    found should equal(blake2)

    val keccak = "ac501ee78bc9b9429f6b923953946606b260a8de141eb253567342b678bc5f10"
    found = Engine.hash(data, KECCAK).toHex
    found should equal(keccak)

    val sha256 = "c0c77f225dd222144bc4ef79dca00ab7d955f26da2b1e0f25df81f8a7e86917c"
    found = Engine.hash(data, SHA_256).toHex
    found should equal(sha256)
//
//    val sha3 = "9d6bf5763cb18bceb7c15270ff8400ae70bf3cd71928463a30f02805d913409d"
//    found = Engine.hash(data, SHA_3).toHex
//    found should equal(sha3)
  }
}
