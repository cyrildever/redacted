package fr.edgewhere.redacted

import fr.edgewhere.redacted.client._

/**
 * Main application entry point
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 1.0
 */
object Main extends App {
    try {
        // Prepare operation
        val config = Config.init(args = args)
        if (!config.checks) throw new Exception("bad arguments: verify usage")
        val operation = if (config.expand) Operation.EXPANSION else Operation.REDACTION

        // Process operation
        val worker = Redacted(operation, config)
        worker.process
    } catch {
        case e: Exception =>
            e.getStackTrace foreach println
            println
            println(Config.getUsage)
            System.exit(1)
    }
}
