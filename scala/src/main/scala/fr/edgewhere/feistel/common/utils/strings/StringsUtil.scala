package fr.edgewhere.feistel.common.utils.strings

import fr.edgewhere.feistel.common.exception.NotSameLengthException

/**
 * StringsUtil
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 1.0
 */
object StringsUtil {
  def add(str1: String, str2: String): String =
    if (str1.length != str2.length) throw NotSameLengthException("to be added, strings must be of the same length")
    else str1.zipWithIndex.foldLeft("")((acc, item) => acc + (item._1.toInt + str2.charAt(item._2).toInt).toChar)

  def extract(from: String, startIndex: Int, desiredLength: Int): String = {
    val actualStartIndex = startIndex % from.length
    val lengthNeeded = actualStartIndex + desiredLength
    val repetitions = lengthNeeded / from.length + 1
    (from * repetitions).substring(actualStartIndex, actualStartIndex + desiredLength)
  }

  def split(item: String): (String, String) = {
    val half = item.length / 2
    (item.substring(0, half), item.substring(half))
  }
}
