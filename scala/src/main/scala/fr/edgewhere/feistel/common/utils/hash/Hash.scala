package fr.edgewhere.feistel.common.utils.hash

import javax.xml.bind.DatatypeConverter

/**
 * Hash type
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 1.0
 */
object Hash {
  type Hash = Seq[Byte]

  implicit class HashOps(h: Hash) {
    def toHex: String = DatatypeConverter.printHexBinary(h.toArray).toLowerCase
  }

  def fromHex(str: String): Hash = DatatypeConverter.parseHexBinary(str)
}
