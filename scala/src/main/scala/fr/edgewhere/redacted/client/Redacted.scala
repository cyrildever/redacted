package fr.edgewhere.redacted.client

import fr.edgewhere.feistel.common.utils.hash.Engine._
import fr.edgewhere.feistel.Feistel
import fr.edgewhere.redacted.client.Config._
import fr.edgewhere.redacted.client.Operation._
import fr.edgewhere.redacted.core.Redactor
import fr.edgewhere.redacted.model.Dictionary.fromFile
import java.io._
import scala.io.Source

/**
 * Redacted class
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 1.0
 *
 * @param operation The type of operation (REDACTION | EXPANSION)
 * @param config The configuration object
 */
final case class Redacted(operation: Operation, config: Config) {
  def process: Boolean = {
    // Prepare processing
    val cipher = Feistel.FPECipher(config.hash.getOrElse(SHA_256), config.key.getOrElse(DEFAULT_KEY), config.rounds.getOrElse(DEFAULT_ROUNDS))
    val dictionary = fromFile(config.dictionary.get)
    val redactor = Redactor(dictionary, config.tag.getOrElse(""), cipher, config.both)

    // Read input
    val src = Source.fromFile(config.input)

    // Prepare output file
    val writer = new BufferedWriter(new FileWriter(config.output))

    try {
      val lines = src.getLines

      // Do process
      if (!config.expand) {
        println("Start redacting...")
        lines.foreach(line => writer.write(redactor.redact(line) + "\n"))
        println("Redacted over. Everything went well ;-)")
      } else {
        println("Start expanding...")
        lines.foreach(line => writer.write(redactor.expand(line) + "\n"))
        println("Expansion over. Everything went well ;-)")
      }
      true
    } catch {
      case e: Exception =>
        println(e)
        e.getStackTrace foreach println
        false
    } finally {
      writer.close()
      src.close
    }
  }
}
