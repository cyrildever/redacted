package fr.edgewhere.redacted

import fr.edgewhere.redacted.client.Config

object Main extends App {
    try {
        // Prepare operation
        val config = Config.init(args = args)
        if (!config.checks) throw new Exception("bad arguments: verify usage")
    } catch {
        case e: Exception =>
            e.getStackTrace foreach println
            println
            println(Config.getUsage)
            System.exit(1)
    }
}
