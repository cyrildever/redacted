package fr.edgewhere.feistel.common.utils.hash

import fr.edgewhere.feistel.common.exception.UnknownEngineException
import scorex.crypto.hash._

/**
 * Engine type and utilities
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 1.0
 */
object Engine {
  import Hash._

  type Engine = String

  val BLAKE2b: Engine = "blake-2b-256"
  val KECCAK: Engine = "keccak-256"
  val SHA_256: Engine = "sha-256"
//  val SHA_3: Engine = "sha3-256" // TODO Available from JDK9

  def isAvailable(engine: String): Boolean =
    engine == BLAKE2b || engine == KECCAK || engine == SHA_256

  def hash(input: String, using: Engine): Hash = using match {
    case BLAKE2b =>
      Blake2b256.hash(input)
    case KECCAK =>
      Keccak256.hash(input)
    case SHA_256 =>
      Sha256.hash(input)
//    case SHA_3 =>
//      MessageDigest.getInstance(SHA_3).digest(input.getBytes)
    case _ => throw UnknownEngineException()
  }
}
