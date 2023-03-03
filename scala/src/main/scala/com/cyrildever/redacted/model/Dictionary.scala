package com.cyrildever.redacted.model

import scala.io.Source
import scala.util.matching.Regex

/**
 * Dictionary defines the list of words to redact
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 2.0
 */
final case class Dictionary(words: List[String]) { self =>
  val punctuation: Regex = "[^a-zA-Z0-9]".r
  val apostropheS: Regex = "'s$".r

  def contains (word: String): Boolean =
    self.words.contains(word) ||
      self.words.contains(punctuation.replaceAllIn(word, "")) ||
      self.words.contains(apostropheS.replaceAllIn(word, ""))

  def isEmpty: Boolean = self.words.isEmpty

  def nonEmpty: Boolean = !isEmpty

  def length: Int = self.words.length

  override def toString: String = self.words.mkString(" ")
}
object Dictionary {
  val EMPTY: Dictionary = Dictionary(List())

  def fromFile(path: String, testEnvironment: Boolean = false): Dictionary =
    toDictionary((if (testEnvironment) Source.fromResource(path) else Source.fromFile(path)).getLines.mkString)

  def toDictionary(str: String, delimiters: String*): Dictionary =
    Dictionary((if (delimiters.isEmpty) List(" ") else delimiters).foldLeft(List(str))((acc, delim) => acc.flatMap(s => s.split(delim))))
}
