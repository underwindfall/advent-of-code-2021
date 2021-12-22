private fun part1(){
    val input = getInputAsTest("21")
    val p = IntArray(2)
    for (i in 0..1) {
        p[i] = input[i].removePrefix("Player ${i + 1} starting position: ").toInt()
    }
    var rolls = 0L
    val s = IntArray(2)
    var i = 0
    var d = 1
    fun next(): Int {
        rolls++
        return d.also { d = d % 100 + 1 }
    }
    while (true) {
        p[i] = (p[i] + next() + next() + next() - 1) % 10 + 1
        s[i] += p[i]
        if (s[i] >= 1000) {
            println("part1 ${s[1 - i] * rolls}")
            break
        }
        i = (i + 1) % 2
    }
}

private fun part2(){
    val input = getInputAsTest("21")
    val p = IntArray(2)
    for (i in 0..1) {
        p[i] = input[i].removePrefix("Player ${i + 1} starting position: ").toInt()
    }
    data class WC(var w1: Long, var w2: Long)
    val dp = Array(11) { Array(11) { Array(21) { arrayOfNulls<WC>(21) } } }
    fun find(p1: Int, p2: Int, s1: Int, s2: Int): WC {
        dp[p1][p2][s1][s2]?.let { return it }
        val c = WC(0, 0)
        for (d1 in 1..3) for (d2 in 1..3) for (d3 in 1..3) {
            val p1n = (p1 + d1 + d2 + d3 - 1) % 10 + 1
            val s1n = s1 + p1n
            if (s1n >= 21) {
                c.w1++
            } else {
                val cn = find(p2, p1n, s2, s1n)
                c.w1 += cn.w2
                c.w2 += cn.w1
            }
        }
        dp[p1][p2][s1][s2] = c
        return c
    }
    val c = find(p[0], p[1], 0, 0)
    println(c)
    println(maxOf(c.w1, c.w2))
}
fun main(){
 part1()
 part2()
}