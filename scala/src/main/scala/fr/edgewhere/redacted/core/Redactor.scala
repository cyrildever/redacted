package fr.edgewhere.redacted.core

import fr.edgewhere.redacted.model._
import fr.edgewhere.feistel.FormatPreservingEncryption
import scala.util.control.Breaks._

import Redactor._

/**
 * Redactor ...
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 1.0
 */
final case class Redactor(dictionary: Dictionary, tag: Tag, cipher: FormatPreservingEncryption, both: Boolean = true) {
  self =>

  def redact(line: String, delimiters: String*): String = {
    line.split(("\\s" ++ delimiters.filter(d => d != "\\s" && d != " " && d != "\t" && d != "\n")).mkString).map(word => {
      var output = word
      breakable {
        if (self.both || self.dictionary.nonEmpty) {
          if (self.dictionary.contains(word)) {
            output = self.cipher.encrypt(word)
            break
          } else if (self.tag.nonEmpty && word.startsWith(self.tag)) {
            output = self.tag + self.cipher.encrypt(word.substring(self.tag.length))
            break
          }
        } else if (!self.both && self.dictionary.isEmpty) {
          if (self.tag.nonEmpty && word.startsWith(self.tag)) {
            output = self.tag + self.cipher.encrypt(word.substring(self.tag.length))
            break
          }
        }
      }
      output
    }).mkString(" ")
  }

  def expand(line: String): String =
    line.split(" ").map(word => {
      var output = word
      breakable {
        if (self.both || self.dictionary.nonEmpty) {
          if (self.tag.nonEmpty && word.startsWith(self.tag)) {
            output = self.tag + self.cipher.decrypt(word.substring(self.tag.length))
            break
          } else if (dictionary.contains(self.cipher.decrypt(word))) {
            output = self.cipher.decrypt(word)
            break
          }
        } else if (!self.both && self.dictionary.isEmpty) {
          if (self.tag.nonEmpty && word.startsWith(self.tag)) {
            output = self.tag + self.cipher.decrypt(word.substring(self.tag.length))
            break
          }
        }
      }
      output
    }).mkString(" ")

  def clean(str: String): String = if (self.tag.nonEmpty) str.replace(self.tag, "") else str
}
object Redactor {
  type Tag = String

  def apply(dictionary: Dictionary, cipher: FormatPreservingEncryption): Redactor = Redactor(dictionary, "", cipher, false)

  def apply(tag: Tag, cipher: FormatPreservingEncryption): Redactor = Redactor(Dictionary.EMPTY, tag, cipher, false)
}
