private fun part1() {
  val input = getInputAsTest("12") { split("\n") }
  val g = HashMap<String, HashSet<String>>()
  for (s in input) {
    val (a, b) = s.split("-")
    g.getOrPut(a) { HashSet() }.add(b)
    g.getOrPut(b) { HashSet() }.add(a)
  }
  var ans = 0
  val vs = HashSet<String>()
  fun find(a: String) {
    if (a == "end") {
      ans++
      return
    }
    for (b in g[a]!!) {
      if (b == "start") continue
      val small = b[0] in 'a'..'z'
      if (small) {
        if (b in vs) continue
        vs += b
      }
      find(b)
      if (small) vs -= b
    }
  }
  find("start")
  println("Part1 $ans")
}

private fun part2() {
  val input = getInputAsTest("12") { split("\n") }
  val g = HashMap<String, HashSet<String>>()
  for (s in input) {
    val (a, b) = s.split("-")
    g.getOrPut(a) { HashSet() }.add(b)
    g.getOrPut(b) { HashSet() }.add(a)
  }
  var ans = 0
  val vs = HashSet<String>()
  fun find(a: String, vt: Boolean) {
    if (a == "end") {
      ans++
      return
    }
    for (b in g[a]!!) {
      if (b == "start") continue
      val small = b[0] in 'a'..'z'
      var nvt = vt
      if (small) {
        if (b in vs) {
          if (vt) continue
          nvt = true
        } else {
          vs += b
        }
      }
      find(b, nvt)
      if (small && nvt == vt) vs -= b
    }
  }
  find("start", false)
  println("Part2 $ans")
}

fun main() {
  part1()
  part2()
}
