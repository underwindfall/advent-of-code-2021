import java.util.*

private fun part1() {
    val input = getInputAsTest("15") { split("\n") }
        .map { it.toCharArray().map { char -> char.digitToInt() } }

    val m = input.size
    val n = input[0].size
    val grid = Array(m) { IntArray(n) { Int.MAX_VALUE } }
    val visited = Array(m) { BooleanArray(n) { false } }


    fun dfs(i: Int, j: Int, sum: Int) {
        if (i < 0 || i >= m || j < 0 || j >= n) return
        grid[i][j] = minOf(grid[i][j], input[i][j] + sum)
    }

    grid[0][0] = 0
    while (!visited[m - 1][n - 1]) {
        var mx = Int.MAX_VALUE
        var mi = -1
        var mj = -1
        for (i in 0 until m)
            for (j in 0 until n) {
                if (!visited[i][j] && grid[i][j] < mx) {
                    mx = grid[i][j]
                    mi = i
                    mj = j
                }
            }

        visited[mi][mj] = true
        dfs(mi - 1, mj, mx)
        dfs(mi + 1, mj, mx)
        dfs(mi, mj - 1, mx)
        dfs(mi, mj + 1, mx)
    }
    println("Part1 ${grid[m - 1][n - 1]}")
}

//too slow :(
//only find a way to how to build the grid
private fun part2() {
    val input = getInputAsTest("15") { split("\n") }
        .map { it.toCharArray().map { char -> char.digitToInt() } }
    val m0 = input.size
    val n0 = input[0].size
    val m = 5 * m0
    val n = 5 * n0
    val newInput = Array(m) { i ->
        IntArray(n) { j ->
            val k = i / m0 + j / n0
            (input[i % m0][j % n0] + k - 1) % 9 + 1
        }
    }
    //trick part is how to build the map
    val grid = Array(m) { IntArray(n) { Int.MAX_VALUE } }
    val visited = Array(m) { BooleanArray(n) { false } }

    fun dfs(i: Int, j: Int, sum: Int) {
        if (i < 0 || i >= m || j < 0 || j >= n) return
        if (visited[i][j]) return
        grid[i][j] = minOf(grid[i][j], newInput[i][j] + sum)
    }


    grid[0][0] = 0
    while (!visited[m - 1][n - 1]) {
        var mx = Int.MAX_VALUE
        var mi = -1
        var mj = -1
        for (i in 0 until m)
            for (j in 0 until n) {
                if (!visited[i][j] && grid[i][j] < mx) {
                    mx = grid[i][j]
                    mi = i
                    mj = j
                }
            }

        visited[mi][mj] = true
        dfs(mi - 1, mj, mx)
        dfs(mi + 1, mj, mx)
        dfs(mi, mj - 1, mx)
        dfs(mi, mj + 1, mx)
    }


    println("Part2 ${grid[m - 1][n - 1]}")
}

/**
 * optimized by add heap to find the min value
 */
private fun part2_heap() {
    data class Point(val i: Int, val j: Int, val sum: Int)

    val input = getInputAsTest("15") { split("\n") }
        .map { it.toCharArray().map { char -> char.digitToInt() } }
    val m0 = input.size
    val n0 = input[0].size
    val m = 5 * m0
    val n = 5 * n0
    //trick part is how to build the map
    val newInput = Array(m) { i ->
        IntArray(n) { j ->
            val k = i / m0 + j / n0
            (input[i % m0][j % n0] + k - 1) % 9 + 1
        }
    }
    val visited = Array(m) { BooleanArray(n) { false } }
    val grid = Array(m) { IntArray(n) { Int.MAX_VALUE } }

    val pq = PriorityQueue(compareBy(Point::sum))

    fun dfs(i: Int, j: Int, sum: Int) {
        if (i < 0 || i >= m || j < 0 || j >= n) return
        if (visited[i][j]) return
        val newSum = sum + newInput[i][j]
        if (newSum < grid[i][j]) {
            grid[i][j] = newSum
            pq.add(Point(i, j, newSum))
        }
    }

    grid[0][0] = 0

    pq.add(Point(0, 0, 0))

    while (!visited[m - 1][n - 1]) {
        val point = pq.poll()
        if (visited[point.i][point.j]) continue
        visited[point.i][point.j] = true
        dfs(point.i - 1, point.j, point.sum)
        dfs(point.i + 1, point.j, point.sum)
        dfs(point.i, point.j - 1, point.sum)
        dfs(point.i, point.j + 1, point.sum)
    }
    println("Part2 ${grid[m - 1][n - 1]}")
}


fun main() {
    part1()
//    part2()
    part2_heap()
}