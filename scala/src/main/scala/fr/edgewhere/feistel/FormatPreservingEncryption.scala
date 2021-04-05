package fr.edgewhere.feistel

/**
 * FormatPreservingEncryption interface
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 1.0
 */
trait FormatPreservingEncryption {
  def encrypt(str: String): String
  def decrypt(str: String): String
}
