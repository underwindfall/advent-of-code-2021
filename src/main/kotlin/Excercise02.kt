private fun part1() {
  getInputAsTest("02") { split("\n") }
    .map { it.split(" ") }
    .map { it[0] to it[1].toInt() }
    .fold(0 to 0) { (y, x), (move, unit) ->
      when (move) {
        "forward" -> y to x + unit
        "down" -> y + unit to x
        "up" -> y - unit to x
        else -> throw IllegalArgumentException("Unknown move: $move")
      }
    }
    .apply { println("Part 1: ${this.first * this.second}") }
}

private fun part2() {
  getInputAsTest("02") { split("\n") }
    .map { it.split(" ") }
    .map { it[0] to it[1].toInt() }
    .fold(Triple(0, 0, 0)) { (depth, horizontal, aim), (move, unit) ->
      when (move) {
        "forward" -> Triple(depth + (aim * unit), horizontal + unit, aim)
        "up" -> Triple(depth, horizontal, aim - unit)
        else -> Triple(depth, horizontal, aim + unit)
      }
    }
    .apply { println("Part 2: ${this.first * this.second}") }
}

fun main() {
  part1()
  part2()
}
