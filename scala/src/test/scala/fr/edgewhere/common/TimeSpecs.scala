package fr.edgewhere.common

import fr.edgewhere.BasicUnitSpecs
import fr.edgewhere.common.Time.time

class TimeSpecs extends BasicUnitSpecs {
  "Time.time" should "display the elapsed time in milliseconds" in {
    val list = time { 1 to 1000 by 1 toList }
    list should have size 1000
  }
}
