private fun part1() {
  val input = getInputAsTest("10")
  var ans = 0
  for (ss in input) {
    val s = ArrayList<Char>()
    for (c in ss) {
      when (c) {
        '(' -> s += ')'
        '[' -> s += ']'
        '{' -> s += '}'
        '<' -> s += '>'
        else -> {
          if (s.isEmpty()) break
          val cc = s.removeLast()
          if (cc != c) {
            when (c) {
              ')' -> ans += 3
              ']' -> ans += 57
              '}' -> ans += 1197
              '>' -> ans += 25137
              else -> error("!!!")
            }
            break
          }
        }
      }
    }
  }
  println("Part1 $ans")
}

private fun part2() {
  val input = getInputAsTest("10")
  val a = ArrayList<Long>()
  for (ss in input) {
    val s = ArrayList<Char>()
    var ok = true
    for (c in ss) {
      when (c) {
        '(' -> s += ')'
        '[' -> s += ']'
        '{' -> s += '}'
        '<' -> s += '>'
        else -> {
          if (s.isEmpty()) {
            ok = false
            break
          }
          val cc = s.removeLast()
          if (cc != c) {
            ok = false
            break
          }
        }
      }
    }
    if (ok && s.isNotEmpty()) {
      var sc = 0L
      while (s.isNotEmpty()) {
        val c = s.removeLast()
        sc *= 5
        when (c) {
          ')' -> sc += 1
          ']' -> sc += 2
          '}' -> sc += 3
          '>' -> sc += 4
        }
      }
      a += sc
    }
  }
  a.sort()
  println("Part2 ${a[a.size / 2]}")
}

fun main() {
  part1()
  part2()
}
