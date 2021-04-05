package fr.edgewhere.common

/**
 * Time utility class
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 1.0
 */
object Time {

  /**
   * Execute code and print elapsed time
   *
   * @example {{{
   *  // Elapsed time: 0.182843ms
   *  val list = time { 1 to 1000 by 1 toList }
   * }}}
   */
  def time[R](block: => R): R = {
    val t0 = System.nanoTime()
    val result = block    // call-by-name
    val t1 = System.nanoTime()
    println("Elapsed time: " + (t1 - t0)/1e6 + "ms")
    result
  }
}
