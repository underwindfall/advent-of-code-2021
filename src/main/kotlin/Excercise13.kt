private fun part1() {
  val input = getInputAsTest("13")
  var a =
    input
      .takeWhile { it.isNotEmpty() }
      .map { it.split(",").map { it.toInt() }.let { (x, y) -> Point(x, y) } }
      .toSet()

  val i = input.indexOf("") + 1
  val s = input[i]
  val pr = "fold along "
  check(s.startsWith(pr))
  val ss = s.substring(pr.length)
  val z = ss.substringAfter('=').toInt()
  a =
    when (ss[0]) {
      'x' -> {
        a
          .map { p ->
            if (p.x > z) {
              Point(2 * z - p.x, p.y)
            } else p
          }
          .toSet()
      }
      'y' -> {
        a.map { p -> if (p.y > z) Point(p.x, p.y - z) else p }.toSet()
      }
      else -> error(ss)
    }

  println("Part1 ${a.size}")
}

private fun part2() {
  val input = getInputAsTest("13")
  var a =
    input
      .takeWhile { it.isNotEmpty() }
      .map { it.split(",").map { it.toInt() }.let { (x, y) -> Point(x, y) } }
      .toSet()
  for (i in input.indexOf("") + 1 until input.size) {
    val s = input[i]
    val pr = "fold along "
    check(s.startsWith(pr))
    val ss = s.substring(pr.length)
    val z = ss.substringAfter('=').toInt()
    when (ss[0]) {
      'x' -> {
        a =
          a
            .map { p ->
              if (p.x > z) {
                Point(2 * z - p.x, p.y)
              } else p
            }
            .toSet()
      }
      'y' -> {
        a =
          a
            .map { p ->
              if (p.y > z) {
                Point(p.x, 2 * z - p.y)
              } else p
            }
            .toSet()
      }
      else -> error(ss)
    }
  }
  println("Part2 ${a.size}")
  val b = Array(6) { CharArray(40) { '.' } }
  for (p in a) {
      b[p.y][p.x] = '█'
  }

  for (c in b) println(c.concatToString())
}

data class Point(val x: Int, val y: Int)

fun main() {
  part1()
  part2()
}
