private fun part1() {
  val input =
    getInputAsTest("11") { split("\n") }.map { it.map { it.digitToInt() }.toMutableList() }
  val n = input.size
  val m = input[0].size
  var ans = 0
  repeat(100) {
    val q = ArrayList<Pos>()
    fun inc(i: Int, j: Int) {
      input[i][j]++
      if (input[i][j] == 10) {
        q += Pos(i, j)
      }
    }
    for (i in 0 until n) for (j in 0 until m) inc(i, j)
    var qh = 0
    while (qh < q.size) {
      val (i, j) = q[qh++]
      for (di in -1..1) for (dj in -1..1) if (di != 0 || dj != 0) {
        val i1 = i + di
        val j1 = j + dj
        if (i1 in 0 until n && j1 in 0 until m) inc(i1, j1)
      }
    }
    ans += q.size
    for (p in q) {
      input[p.i][p.j] = 0
    }
  }
  println("Part1 $ans")
}

private fun part2() {
  val input =
    getInputAsTest("11") { split("\n") }.map { it.map { it.digitToInt() }.toMutableList() }
  val n = input.size
  val m = input[0].size
  var ans = 0
  var step = 0
  while (true) {
    step++
    val q = ArrayList<Pos>()
    fun inc(i: Int, j: Int) {
      input[i][j]++
      if (input[i][j] == 10) {
        q += Pos(i, j)
      }
    }
    for (i in 0 until n) for (j in 0 until m) inc(i, j)
    var qh = 0
    while (qh < q.size) {
      val (i, j) = q[qh++]
      for (di in -1..1) for (dj in -1..1) if (di != 0 || dj != 0) {
        val i1 = i + di
        val j1 = j + dj
        if (i1 in 0 until n && j1 in 0 until m) inc(i1, j1)
      }
    }
    if (q.size == n * m) {
      ans = step
      break
    }
    for (p in q) {
      input[p.i][p.j] = 0
    }
  }
  println("Part2 $ans")
}

data class Pos(val i: Int, val j: Int)

fun main() {
  part1()
  part2()
}
