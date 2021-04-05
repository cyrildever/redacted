package fr.edgewhere.redacted.model

import fr.edgewhere.BasicUnitSpecs

/**
 * DictionarySpecs test class
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version 1.0
 */
class DictionarySpecs extends BasicUnitSpecs {
  import Dictionary._

  "fromFile" should "build the appropriate dictionary from a file" in {
    val dic = fromFile("dictionaryExample.txt", true)
    dic should have length 5
    dic.contains("Dever") should be (true)
    dic.toString should equal ("M. Cyril Antoine Laurent Dever")
  }
  "toDictionary" should "build the appropriate dictionary from a string" in {
    val str = "Cyril Antoine Laurent,Dever"
    var dic = toDictionary(str)
    dic should have length 3
    dic.contains("Dever") should be (false)
    dic = toDictionary(str, " ", ",")
    dic should have length 4
    dic.contains("Dever") should be (true)
    dic.isEmpty should be (false)
    dic.toString should equal ("Cyril Antoine Laurent Dever")
  }
}
