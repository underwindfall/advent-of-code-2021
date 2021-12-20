import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO

private fun part1() {
  val input =
    getInputAsTest("15") { split("\n") }.map { it.toCharArray().map { char -> char.digitToInt() } }

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
    for (i in 0 until m) for (j in 0 until n) {
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

// too slow :(
// only find a way to how to build the grid
private fun part2() {
  val input =
    getInputAsTest("15") { split("\n") }.map { it.toCharArray().map { char -> char.digitToInt() } }
  val m0 = input.size
  val n0 = input[0].size
  val m = 5 * m0
  val n = 5 * n0
  val newInput =
    Array(m) { i ->
      IntArray(n) { j ->
        val k = i / m0 + j / n0
        (input[i % m0][j % n0] + k - 1) % 9 + 1
      }
    }
  // trick part is how to build the map
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
    for (i in 0 until m) for (j in 0 until n) {
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

/** optimized by add heap to find the min value */
private fun part2_heap() {
  data class Point(val i: Int, val j: Int, val sum: Int)

  val input =
    getInputAsTest("15") { split("\n") }.map { it.toCharArray().map { char -> char.digitToInt() } }
  val m0 = input.size
  val n0 = input[0].size
  val m = 5 * m0
  val n = 5 * n0
  // trick part is how to build the map
  val newInput =
    Array(m) { i ->
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

// this part of  code is forked from
// https://github.com/elizarov/AdventOfCode2021/blob/main/src/Day15_2_vis.kt
// ffmpeg -framerate 50 -i vis/Day15_%06d.png -c:v libx265 Day15vis.mp4
private fun part2_visualization() {
  val emptyColor = 0x000000
  val queuedColor = 0x00ff00
  val visitedMod = 100
  val imageStep = 100
  val visitedColor = IntArray(visitedMod) { i -> 0xff0000 + 0x0000ff * i / (visitedMod - 1) }
  val dir = File("vis")
  dir.mkdirs()

  val dayId = "15"
  val a0 =
    getInputAsTest("15") { split("\n") }.map { it.toCharArray().map { char -> char.digitToInt() } }
  val n0 = a0.size
  val m0 = a0[0].size
  val n = 5 * n0
  val m = 5 * m0
  val a =
    Array(n) { i ->
      IntArray(m) { j ->
        val k = i / n0 + j / m0
        (a0[i % n0][j % m0] + k - 1) % 9 + 1
      }
    }
  val d = Array(n) { IntArray(m) { Int.MAX_VALUE } }
  data class Pos(val i: Int, val j: Int, val x: Int)
  val v = Array(n) { BooleanArray(m) }
  val q = PriorityQueue(compareBy(Pos::x))
  val image = BufferedImage(m, n, BufferedImage.TYPE_INT_RGB)
  for (j in 0 until m) for (i in 0 until n) image.setRGB(j, i, emptyColor)
  var imageIndex = 1
  fun flushImage() {
    val file = File(dir, "Day15_${imageIndex++.toString().padStart(6, '0')}.png")
    println("Writing $file")
    ImageIO.write(image, "png", file)
  }
  flushImage()
  var stepIndex = 0
  fun enqueue(i: Int, j: Int, x: Int) {
    d[i][j] = x
    q.add(Pos(i, j, x))
    image.setRGB(j, i, queuedColor)
    if (stepIndex++ % imageStep == 0) flushImage()
  }
  fun relax(i: Int, j: Int, x: Int) {
    if (i !in 0 until n || j !in 0 until m || v[i][j]) return
    val xx = x + a[i][j]
    if (xx < d[i][j]) enqueue(i, j, xx)
  }
  enqueue(0, 0, 0)
  while (!v[n - 1][m - 1]) {
    val (i, j, x) = q.remove()
    if (v[i][j]) continue
    image.setRGB(j, i, visitedColor[x % visitedMod])
    v[i][j] = true
    relax(i - 1, j, x)
    relax(i + 1, j, x)
    relax(i, j - 1, x)
    relax(i, j + 1, x)
  }
  flushImage()
  val ans = d[n - 1][m - 1]
  println(ans)
}

fun main() {
  part1()
  //    part2()
  //    part2_visualization()
  part2_heap()
}
