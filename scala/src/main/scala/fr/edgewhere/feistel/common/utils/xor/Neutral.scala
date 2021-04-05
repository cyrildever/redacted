package fr.edgewhere.feistel.common.utils.xor

/**
 * Neutral for XOR operation
 */
final case class Neutral(size: Int) { self =>
  override def toString: String = Seq[Byte](0).map(_.toChar).mkString * self.size
}

