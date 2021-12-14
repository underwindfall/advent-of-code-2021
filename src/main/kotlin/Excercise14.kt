private fun part1() {
  val input = getInputAsTest("14")
  val map = input.drop(2).map { it.split("->") }.associate { it[0].trim() to it[1].trim() }
  var pairFreq = mutableMapOf<String, Long>()
  input[0].windowed(2).forEach { key -> pairFreq[key] = pairFreq.getOrDefault(key, 0) + 1 }

  repeat(10) {
    val newMap = mutableMapOf<String, Long>()
    pairFreq.forEach { (k, v) ->
      newMap[k[0] + map[k]!!] = newMap.getOrDefault(k[0] + map[k]!!, 0) + v
      newMap[map[k]!! + k[1]] = newMap.getOrDefault(map[k]!! + k[1], 0) + v
    }
    pairFreq = newMap
  }

  val charFreq = mutableMapOf<Char, Long>()
  pairFreq.forEach { (k, v) -> charFreq[k[0]] = charFreq.getOrDefault(k[0], 0) + v }
  charFreq[input[0].last()] = charFreq.getOrDefault(input[0].last(), 0) + 1
  val ans = charFreq.values.sorted().let { it.last() - it.first() }
  println("Part 1: $ans")
}

private fun part2() {
  val input = getInputAsTest("14")
  val map = input.drop(2).map { it.split("->") }.associate { it[0].trim() to it[1].trim() }
  var pairFreq = mutableMapOf<String, Long>()
  input[0].windowed(2).forEach { key -> pairFreq[key] = pairFreq.getOrDefault(key, 0) + 1 }

  repeat(40) {
    val newMap = mutableMapOf<String, Long>()
    pairFreq.forEach { (k, v) ->
      newMap[k[0] + map[k]!!] = newMap.getOrDefault(k[0] + map[k]!!, 0) + v
      newMap[map[k]!! + k[1]] = newMap.getOrDefault(map[k]!! + k[1], 0) + v
    }
    pairFreq = newMap
  }

  val charFreq = mutableMapOf<Char, Long>()
  pairFreq.forEach { (k, v) -> charFreq[k[0]] = charFreq.getOrDefault(k[0], 0) + v }
  charFreq[input[0].last()] = charFreq.getOrDefault(input[0].last(), 0) + 1
  val ans = charFreq.values.sorted().let { it.last() - it.first() }
  println("Part 2: $ans")
}

fun main() {
  part1()
  part2()
}
