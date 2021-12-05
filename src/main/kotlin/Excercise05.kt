import kotlin.math.abs

private fun part1() {
  getInputAsTest("05") { split("\n") }
    .map { preProcess(it) }
    .let { input ->
      buildMap<EntryPoint, Int> {
        for ((start, end) in input) {
          val (x0, y0) = start
          val (x1, y1) = end
          when {
            x0 == x1 ->
              for (y in minOf(y0, y1)..maxOf(y0, y1)) put(x0 to y, getOrElse(x0 to y) { 0 } + 1)
            y0 == y1 ->
              for (x in minOf(x0, x1)..maxOf(x0, x1)) put(x to y0, getOrElse(x to y0) { 0 } + 1)
          }
        }
      }
        .values
        .count { it > 1 }
    }
    .apply { println("Part1 $this") }
}

private fun part2() {
  getInputAsTest("05") { split("\n") }
    .map { preProcess(it) }
    .filter { (start, end) ->
      val (x0, y0) = start
      val (x1, y1) = end
      x0 == x1 || y0 == y1 || abs(y0 - y1) == abs(x0 - x1)
    }
    .let { input ->
      buildMap<EntryPoint, Int> {
        for ((start, end) in input) {
          val (x0, y0) = start
          val (x1, y1) = end
          when {
            x0 == x1 ->
              for (y in minOf(y0, y1)..maxOf(y0, y1)) {
                put(x0 to y, getOrElse(x0 to y) { 0 } + 1)
              }
            y0 == y1 ->
              for (x in minOf(x0, x1)..maxOf(x0, x1)) {
                put(x to y0, getOrElse(x to y0) { 0 } + 1)
              }
            else -> {
              val d = abs(x1 - x0)
              val dx = (x1 - x0) / d
              val dy = (y1 - y0) / d
              repeat(d) { i ->
                val xy = x0 + dx * i to y0 + i * dy
                put(xy, getOrElse(xy) { 0 } + 1)
              }
            }
          }
        }
      }
        .values
        .count { it > 1 }
    }
    .apply { println("Part2 $this") }
}

typealias EntryPoint = Pair<Int, Int>

private fun preProcess(s: String): Pair<EntryPoint, EntryPoint> {
  val (from, to) = s.split("->").map { it.trim().split(",") }.map { it[0].toInt() to it[1].toInt() }
  return Pair(from, to)
}

fun main() {
  part1()
  part2()
}
