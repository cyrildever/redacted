package com.cyrildever.redacted.client

/**
 * Operation defines the type of operation to process
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 2.0
 */
object Operation {
  type Operation = String

  val REDACTION: Operation = "redact"
  val EXPANSION: Operation = "expand"
}
