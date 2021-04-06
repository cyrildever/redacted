package fr.edgewhere.redacted

import fr.edgewhere.redacted.client._

/**
 * Main application entry point
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 1.0
 *
 * @example {{{
 *  $ sbt assembly && java -cp target/scala-2.12/redacted-jar-0.1.1.jar fr.edgewhere.redacted.Main -i=../exampleInput.txt -o=exampleOutput.txt -d=src/test/resources/dictionaryExample.txt -b
 *  $ java -cp target/scala-2.12/redacted-jar-0.1.1.jar fr.edgewhere.redacted.Main -i=exampleOutput.txt -o=exampleRecovery.txt -d=src/test/resources/dictionaryExample.txt -b -x
 * }}}
 */
object Main extends App {
  try {
    // Prepare operation
    val config = Config.init(args = args)
    if (!config.checks) throw new Exception("Bad arguments: verify usage")
    val operation = if (config.expand) Operation.EXPANSION else Operation.REDACTION

    // Process operation
    val worker = Redacted(operation, config)
    if (!worker.process) throw new Exception("Unable to process operation")
  } catch {
    case e: Exception =>
      println(e)
      e.getStackTrace foreach println
      println
      println(Config.getUsage)
      System.exit(1)
  }
}
