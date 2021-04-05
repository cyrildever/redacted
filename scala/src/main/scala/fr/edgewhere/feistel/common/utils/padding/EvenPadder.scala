package fr.edgewhere.feistel.common.utils.padding

/**
 * EvenPadder type and utility to make an item of even length
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 1.0
 */
object EvenPadder {
  val LEFT_PAD_CHARACTER = '\u0002' // Unicode U+0002: start-of-text

  type Padded = String

  def pad(data: String): Padded =
    if (data.length % 2 == 0) data
    else LEFT_PAD_CHARACTER + data

  def unpad(padded: Padded): String =
    if (padded.length != 0 && padded(0) == LEFT_PAD_CHARACTER.toByte) padded.slice(1, padded.length)
    else padded
}
