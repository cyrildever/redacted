package fr.edgewhere.redacted.client

import fr.edgewhere.redacted.client.Config.DEFAULT_ROUNDS
import scopt.{DefaultOParserSetup, OParser, OParserBuilder}

/**
 * Config class for command-line parsing
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 1.0
 *
 * @see Config.parser details for parameter definition
 */
final case class Config(
  input: String,
  output: String,
  both: Boolean = false,
  expand: Boolean = false,
  hash: String = "sha-256",
  rounds: Int = DEFAULT_ROUNDS,
  dictionary: Option[String] = None,
  tag: Option[String] = Some("~"),
  var key: Option[String]= None
) { self =>
  import Config._

  /**
   * @return  the redacted application full version name, eg. 1.0.0
   */
  lazy val appVersion: String = getClass.getPackage.getImplementationVersion

  /**
   * Verify that the passed options and data are valid for handling a full redacted process
   */
  def checks: Boolean = {
    val files = input.nonEmpty && output.nonEmpty
    val mode = (both && tag.isDefined && dictionary.isDefined) ||
      (!both && (tag.isDefined || dictionary.isDefined))
    self.key = if (self.key.isDefined) self.key else Some(DEFAULT_KEY)
    val cipher = self.rounds >= 2 && self.key.nonEmpty /* && isValidEngine(self.hash) */
    files && mode && cipher
  }

  def isEmpty: Boolean = self.input.isEmpty || self.output.isEmpty
}
object Config {
  val DEFAULT_KEY = "d51e1d9a9b12cd88a1d232c1b8730a05c8a65d9706f30cdb8e08b9ed4c7b16a0"
  val DEFAULT_ROUNDS = 10

  private val EMPTY = Config("", "")

  private[Config] var _instance: Config = EMPTY

  /**
   * Initialize configuration using command-line arguments
   */
  def init(args: Array[String]): Config = {
    if (_instance.isEmpty) {
      OParser.parse(parser, args, EMPTY, new DefaultOParserSetup {
        override def showUsageOnError: Some[Boolean] = Some(true)
      }) match {
        case Some(config) => _instance = config
        case _ =>
          println(getUsage)
          throw new Exception("bad arguments")
      }
    }
    _instance
  }

  /**
   * @return the Config instance after initialization
   */
  def get(): Config = if (!_instance.isEmpty) _instance else throw new Exception("config must be initialize first")

  /**
   * Set a new Config if it checks
   *
   * @return `true` if the passed Config was set as the new instance, `false` otherwise
   */
  def set(c: Config): Boolean = if (c.checks) {
    _instance = c
    true
  } else false

  /**
   * @return the command-line usage text
   */
  def getUsage: String = {
    OParser.usage[Config](parser)
  }

  private[Config] lazy val builder: OParserBuilder[Config] = OParser.builder[Config]
  private[Config] lazy val parser: OParser[Unit, Config] = {
    import builder._
    val v = EMPTY.appVersion
    OParser.sequence(
      head("redacted", v),
      programName(s"java -cp redacted-jar-${v}.jar fr.edgewhere.redacted.Main"),
      opt[String]("input")
        .abbr("i")
        .action((x, c) => c.copy(input = x))
        .text("the path to the document to be redacted"),
      opt[String]("output")
        .abbr("o")
        .action((x, c) => c.copy(output = x))
        .text("the name of the output file"),
      opt[Unit]("both")
        .abbr("b")
        .action((x, c) => c.copy(both = true))
        .text("add to use both dictionary and tag"),
      opt[Unit]("expand")
        .abbr("x")
        .action((x, c) => c.copy(expand = true))
        .text("add to expand a redacted document"),
      opt[String]("hashEngine")
        .abbr("h")
        .action((x, c) => c.copy(hash = x))
        .text("the hash engine for the round function"),
      opt[Int]("rounds")
        .abbr("r")
        .action((x, c) => c.copy(rounds = x))
        .text("the number of rounds for the Feistel cipher"),
      opt[String]("dictionary")
        .abbr("d")
        .action((x, c) => c.copy(dictionary = Some(x)))
        .text("the optional path to the dictionary of words to redact"),
      opt[String]("tag")
        .abbr("t")
        .action((x, c) => c.copy(tag = Some(x)))
        .text("the optional tag that prefixes words to redact"),
      opt[String]("key")
        .abbr("k")
        .action((x, c) => c.copy(key = Some(x)))
        .text("the optional key for the FPE scheme (leave it empty to use default)")
    )
  }
}