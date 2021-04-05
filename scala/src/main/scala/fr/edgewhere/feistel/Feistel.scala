package fr.edgewhere.feistel

import fr.edgewhere.feistel.common.exception.WrongCipherParametersException
import fr.edgewhere.feistel.common.utils.base256.Readable
import fr.edgewhere.feistel.common.utils.base256.Readable._
import fr.edgewhere.feistel.common.utils.hash.Engine
import fr.edgewhere.feistel.common.utils.hash.Engine._
import fr.edgewhere.feistel.common.utils.strings.StringsUtil._
import fr.edgewhere.feistel.common.utils.xor.Neutral
import fr.edgewhere.feistel.common.utils.xor.Operation._

/**
 * Feistel object and classes
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 1.0
 */
object Feistel {
  final case class FPECipher(engine: Engine, key: String, rounds: Int) extends FormatPreservingEncryption {
    self =>

    override def encrypt(line: String): Readable =
      if (self.key.isEmpty || self.rounds < 2 || !isAvailable(self.engine)) throw WrongCipherParametersException()
      else if (line.isEmpty) line
      else {
        // Apply the FPE Feistel cipher
        var (left, right) = split(line)
        for (i <- 0 until self.rounds) {
          var (leftRound, rightRound) = (left, right)
          left = right
          if (rightRound.length < leftRound.length) {
            rightRound = rightRound + Neutral(1).toString
          }
          val rnd = round(rightRound, i)
          val (tmp, crop) = if (leftRound.length + 1 == rnd.length) {
            (leftRound + Neutral(1).toString, true)
          } else (leftRound, false)
          right = tmp ^ rnd
          if (crop) {
            right = right.substring(0, right.length-1)
          }
        }
        Readable(left.getBytes ++ right.getBytes)
      }

    override def decrypt(ciphered: Readable): String =
      if (self.key.isEmpty || self.rounds < 2 || !isAvailable(self.engine)) throw WrongCipherParametersException()
      else if (ciphered.isEmpty) ""
      else {
        // Apply the FPE Feistel cipher
        var (left, right) = split(ciphered.string)
        if (self.rounds % 2 != 0 && left.length != right.length) {
          left = left + right.substring(0, 1)
          right = right.substring(1)
        }
        for (i <- 0 until self.rounds) {
          var (leftRound, rightRound) = (left, right)
          if (left.length < right.length) {
            leftRound = leftRound + Neutral(1).toString
          }
          right = left
          val rnd = round(leftRound, self.rounds - i - 1)
          val (tmp, extended) = if (rightRound.length + 1 == rnd.length) {
            rightRound = rightRound + left.substring(left.length - 1)
            (rightRound ^ rnd, true)
          } else {
            (rightRound ^ rnd, false)
          }
          left = if (extended) tmp.substring(0, tmp.length - 1) else tmp
        }
        left + right
      }

    private[FPECipher] def round(item: String, index: Int): String = {
      import fr.edgewhere.feistel.common.utils.hash.Hash._

      val addition = add(item, extract(self.key, index, item.length))
      val hashed = Engine.hash(addition, self.engine)
      extract(hashed.toHex, index, item.length)
    }
  }
}
