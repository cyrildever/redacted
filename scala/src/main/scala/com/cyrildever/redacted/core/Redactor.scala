package com.cyrildever.redacted.core

import com.cyrildever.feistel.FormatPreservingEncryption
import com.cyrildever.redacted.model.Dictionary
import Redactor._
import scala.util.control.Breaks._

/**
 * Redactor class
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 2.0
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

  val DEFAULT_TAG: Tag = "~"

  def apply(dictionary: Dictionary, cipher: FormatPreservingEncryption): Redactor = Redactor(dictionary, "", cipher, both = false)

  def apply(tag: Tag, cipher: FormatPreservingEncryption): Redactor = Redactor(Dictionary.EMPTY, tag, cipher, both = false)
}
