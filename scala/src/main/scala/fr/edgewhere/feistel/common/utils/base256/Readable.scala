package fr.edgewhere.feistel.common.utils.base256

import javax.xml.bind.DatatypeConverter

/**
 * Readable class
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 1.0
 */
object Readable {
  def apply(str: String): Readable = str
  def apply(bytes: Seq[Byte]): Readable = bytes.foldLeft("")((acc, b) => acc + charAt(b.intValue))

  type Readable = String
  implicit class ReadableOps(r: Readable) {
    /**
     * @return the underlying byte array
     */
    def bytes: Array[Byte] = r.map(indexOf(_).toByte).toArray // TODO getBytes couldn't be overridden; find out why

    /**
     * @return the underlying length
     */
    def length: Int = bytes.length

    /**
     * @return the underlying string
     */
    def string: String = r.bytes.map(_.toChar).mkString // TODO toString couldn't be overridden either

    def toHex: String = DatatypeConverter.printHexBinary(bytes).toLowerCase
  }

  def fromHex(str: String): Readable =
    Readable(DatatypeConverter.parseHexBinary(str).map(b => charAt(b.toInt)).mkString)

  val CHARSET = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^`abcdefghijklmnopqrstuvwxyz{|}€¡¢£¤¥¦§¨©ª«¬®¯°±²³´µ¶·¸¹»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷ùúûüýÿăąĊčđĕĘğħĩĭıĵķĿŀŁłňŋŏœŖřŝşŦŧũūůŲŵſƀƁƂƄƆƇƔƕƗƙƛƜƟƢƥƦƧƩƪƭƮưƱƲƵƸƺƾǀǁǂƿǬǮǵǶǹǻǿ"

  /**
   * Returns the character in the Base-256 character set at the passed index
   *
   * @param index The searched index
   * @return the character as a `String` or an empty character if out of bounds
   */
  def charAt(index: Int): String =
    if (index >= 0 && index < CHARSET.length) CHARSET.charAt(index).toString
    else ""

  /**
   * Returns the index of the passed character in the Base-256 character set
   *
   * @param char The searched character
   * @return a positive index if found or -1
   */
  def indexOf(char: Char): Int = CHARSET.indexOf(char)
}
