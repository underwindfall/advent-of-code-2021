import kotlin.math.abs

private fun part1() {
  getInputAsTest("07") { split(",") }.map { it.toInt() }.minFuel().let { println("Part 1 $it") }
}

private fun part2() {
  getInputAsTest("07") { split(",") }.map { it.toInt() }.maxFuel().let { println("Part 2 $it") }
}

private fun List<Int>.minFuel(): Int {
  val minValue = minOf { it }
  val maxValue = maxOf { it }
  return (minValue..maxValue).minOf { meetingPos ->
    sumOf { startPos -> abs(startPos - meetingPos) }
  }
}

private fun List<Int>.maxFuel(): Int {
  val minValue = minOf { it }
  val maxValue = maxOf { it }
  return (minValue..maxValue).minOf { meetingPos ->
    sumOf { startPos ->
      val diff = abs(startPos - meetingPos)
      (1 + diff) * diff / 2
    }
  }
}

fun main() {
  part1()
  part2()
}
