private fun part1() {
  var ans = 0
  val input = getInputAsTest("09") { split("\n") }
  val n = input.size
  val m = input[0].length
  for (i in 0 until n) {
    for (j in 0 until m) {
      val b =
        listOfNotNull(
          input[i].getOrNull(j + 1),
          input[i].getOrNull(j - 1),
          input.getOrNull(i - 1)?.get(j),
          input.getOrNull(i + 1)?.get(j)
        )
      if (input[i][j] < b.minOrNull()!!) {
        ans += 1 + (input[i][j] - '0')
      }
    }
  }

  println("Part 1 $ans")
}

private fun part2() {
  val ans = mutableListOf<Int>()
  val input = getInputAsTest("09") { split("\n") }
  val n = input.size
  val m = input[0].length
  val u = Array(n) { BooleanArray(m) }
  fun dfs(i: Int, j: Int): Int {
    if (i < 0 || i >= n || j < 0 || j >= m || u[i][j] || input[i][j] == '9') return 0
    u[i][j] = true
    return 1 + dfs(i - 1, j) + dfs(i + 1, j) + dfs(i, j - 1) + dfs(i, j + 1)
  }
  for (i in 0 until n) {
    for (j in 0 until m) {
      if (!u[i][j] && input[i][j] != '9') {
        ans += dfs(i, j)
      }
    }
  }
  ans.sortDescending()
  println("Part 2 ${ans[0] * ans[1] * ans[2]}")
}

fun main() {
  part1()
  part2()
}
