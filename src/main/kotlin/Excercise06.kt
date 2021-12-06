import java.math.BigInteger

private fun getSpawnCount(
  memo: MutableMap<Pair<Int, Int>, BigInteger>,
  timer: Int,
  days: Int
): BigInteger {
  val memoKey = Pair(timer, days)
  if (timer >= days) return BigInteger("1")
  if (memo.containsKey(memoKey)) return memo.getValue(memoKey)

  memo[memoKey] =
    getSpawnCount(memo, 6, days - timer - 1) + getSpawnCount(memo, 8, days - timer - 1)
  return memo.getValue(memoKey)
}

private fun part1() {
  val memo = mutableMapOf<Pair<Int, Int>, BigInteger>()
  getInputAsTest("06") { split(",") }
    .map { it.toInt() }
    .map { getSpawnCount(memo, it, 80) }
    .reduce { acc, bigInteger -> acc + bigInteger }
    .let { println("Part1 $it") }
}

private fun part2() {
  val memo = mutableMapOf<Pair<Int, Int>, BigInteger>()
  getInputAsTest("06") { split(",") }
    .map { it.toInt() }
    .map { getSpawnCount(memo, it, 256) }
    .reduce { acc, bigInteger -> acc + bigInteger }
    .let { println("Part1 $it") }
}

fun main() {
  part1()
  part2()
}
