import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.math.abs

data class Burrow(
    val roomSize: Int,
    val hallway: Map<Int, Char>,
    val rooms: Map<Int, List<Char>>,
)

val roomAssignments = mapOf(2 to 'A', 4 to 'B', 6 to 'C', 8 to 'D')

fun getPossibleMoves(burrow: Burrow): List<Pair<Int, Int>> = buildList {
    fun pathExists(i1: Int, i2: Int): Boolean {
        val lo = minOf(i1, i2)
        val hi = maxOf(i1, i2)
        return burrow.hallway.all { (index, value) ->
            index !in (lo + 1)..(hi - 1) || value == '.'
        }
    }

    burrow.hallway.forEach { (hindex, hvalue) ->
        burrow.rooms.forEach inner@{ (rindex, rvalue) ->
            // Both empty
            if (hvalue == '.' && rvalue.isEmpty()) {
                return@inner
            }

            // Hallway to room (room compatible)
            if (hvalue != '.') {
                if (hvalue == roomAssignments[rindex]
                    && rvalue.all { it == hvalue }
                    && pathExists(hindex, rindex)
                ) {
                    add(hindex to rindex)
                }
                return@inner
            }

            // Room to hallway (hallway empty)
            if (pathExists(rindex, hindex)
                && rvalue.any { it != roomAssignments[rindex] }
            ) {
                add(rindex to hindex)
            }
        }
    }
}

fun execute(burrow: Burrow, move: Pair<Int, Int>): Pair<Burrow, Int> {
    fun costFor(amphipod: Char) = when (amphipod) {
        'A' -> 1
        'B' -> 10
        'C' -> 100
        'D' -> 1000
        else -> error("Invalid amphipod provided")
    }

    fun roomToHallway(burrow: Burrow, ri: Int, hi: Int): Pair<Burrow, Int> {
        val room = burrow.rooms.getValue(ri)
        val amphipod = room.first()

        val upwards = burrow.roomSize - room.size + 1
        val sideways = abs(ri - hi)
        val cost = (upwards + sideways) * costFor(amphipod)

        val modifiedBurrow = burrow.copy(
            rooms = burrow.rooms.plus(ri to room.drop(1)),
            hallway = burrow.hallway.plus(hi to amphipod),
        )

        return Pair(modifiedBurrow, cost)
    }

    fun hallwayToRoom(burrow: Burrow, hi: Int, ri: Int): Pair<Burrow, Int> {
        val room = burrow.rooms.getValue(ri)
        val amphipod = burrow.hallway.getValue(hi)

        val sideways = abs(ri - hi)
        val downwards = burrow.roomSize - room.size
        val cost = (sideways + downwards) * costFor(amphipod)

        val modifiedBurrow = burrow.copy(
            rooms = burrow.rooms.plus(ri to listOf(amphipod) + room),
            hallway = burrow.hallway.plus(hi to '.'),
        )

        return Pair(modifiedBurrow, cost)
    }

    return if (move.first in roomAssignments.keys) {
        roomToHallway(burrow, move.first, move.second)
    } else {
        hallwayToRoom(burrow, move.first, move.second)
    }
}

fun organizeAmphipods(input: List<String>): Int {
    val rooms = input.drop(2).dropLast(1).map { line ->
        line.filter { char -> char in 'A'..'D' }
    }
    val a = rooms.map { it[0] }
    val b = rooms.map { it[1] }
    val c = rooms.map { it[2] }
    val d = rooms.map { it[3] }

    val initialBurrow = Burrow(
        roomSize = a.size,
        hallway = mapOf(
            0 to '.',
            1 to '.',
            3 to '.',
            5 to '.',
            7 to '.',
            9 to '.',
            10 to '.',
        ),
        rooms = mapOf(
            2 to a,
            4 to b,
            6 to c,
            8 to d,
        ),
    )

    var bestCost = Int.MAX_VALUE
    val seen = mutableMapOf<Burrow, Int>()

    fun simulate(prevBurrow: Burrow, move: Pair<Int, Int>, prevCost: Int): Int {
        if (prevCost >= bestCost) return Int.MAX_VALUE

        val (burrow, cost) = execute(prevBurrow, move)
        val totalCost = prevCost + cost

        if (burrow in seen) {
            if (seen.getValue(burrow) <= totalCost) {
                return Int.MAX_VALUE
            }
        }
        seen[burrow] = totalCost

        if (roomAssignments.all { (ri, amph) ->
                val r = burrow.rooms.getValue(ri)
                r.size == burrow.roomSize && r.all { it == amph }
            }) {
            if (totalCost < bestCost) bestCost = totalCost
            return totalCost
        }

        val moves = getPossibleMoves(burrow)
        if (moves.isEmpty()) return Int.MAX_VALUE
        return moves.minOf { simulate(burrow, it, totalCost) }
    }

    return getPossibleMoves(initialBurrow).minOf { simulate(initialBurrow, it, 0) }
}

private fun part1() {
    val testInput1 = getInputAsTest("23")
    println("part1 ${organizeAmphipods(testInput1)}")
}

private fun part2() {
    data class Cfg(val c: Array<CharArray>, val d: Int) {
        override fun equals(other: Any?): Boolean = other is Cfg && (0..4).all { i -> c[i].contentEquals(other.c[i]) }
        override fun hashCode(): Int = (0..4).fold(0) { a, i-> a * 31 + c[i].contentHashCode() }
        fun copy(d1: Int) = Cfg(c.map { it.copyOf() }.toTypedArray(), d1)
        override fun toString(): String = buildList {
            add("#".repeat(13))
            addAll(c.map { it.concatToString() })
            add("distance=$d")
        }.joinToString("\n")
    }
    val d = HashMap<Cfg,Int>()
    val q = PriorityQueue(compareBy(Cfg::d))
    val f = HashSet<Cfg>()
    fun enqueue(c: Cfg) {
        val d0 = d[c] ?: Int.MAX_VALUE
        if (c.d >= d0) return
        d[c] = c.d
        q += c
    }
    val input1 = getInputAsTest("23").subList(1, 4)
    val input2 = buildList<String> {
        add(input1[0])
        add(input1[1])
        add("  #D#C#B#A#  ")
        add("  #D#B#A#C#  ")
        add(input1[2])
    }
    val start = Cfg(input2.map { it.toCharArray() }.toTypedArray(), 0)
    println(start)
    enqueue(start)
    fun cost(c0: Char): Int = when(c0) {
        'A' -> 1
        'B' -> 10
        'C' -> 100
        'D' -> 1000
        else -> error("$c0")
    }
    while (true) {
        val c = q.remove()!!
        if (c in f) continue
        f += c
        val d0 = d[c]!!
        if (f.size % 10000 == 0) println("d=$d0, qs=${q.size}, fs=${f.size}")
        var ok = true
        check@for (r in 1..4) for (i in 0..3) if (c.c[r][2 * i + 3] != 'A' + i) {
            ok = false
            break@check
        }
        if (ok) {
            println(c)
            break
        }
        for (j0 in 1..11) {
            val c0 = c.c[0][j0]
            if (c0 !in 'A'..'D') continue
            val i = (c0 - 'A')
            val j1 = 2 * i + 3
            if (!(minOf(j0, j1) + 1..maxOf(j0, j1) - 1).all { j -> c.c[0][j] == '.' }) continue
            if (!(1..4).all { r -> c.c[r][j1] == '.' || c.c[r][j1] == c0 }) continue
            val r1 = (4 downTo 1).first { r -> c.c[r][j1] == '.' }
            val c1 = c.copy(d0 + cost(c0) * (abs(j1 - j0) + r1))
            c1.c[0][j0] = '.'
            c1.c[r1][j1] = c0
            enqueue(c1)
        }
        for (i in 0..3) for (r0 in 1..4) {
            val j0 = 2 * i + 3
            val c0 = c.c[r0][j0]
            if (c0 !in 'A'..'D') continue
            if (!(1..r0 - 1).all { r -> c.c[r][j0] == '.' }) continue
            for (j1 in 1..11) {
                if ((j1 - 3) % 2 == 0 && (j1 - 3) / 2 in 0..3) continue
                if (!(minOf(j1, j0)..maxOf(j1, j0)).all { j -> c.c[0][j] == '.' }) continue
                val c1 = c.copy(d0 + cost(c0) * (abs(j1 - j0) + r0))
                c1.c[r0][j0] = '.'
                c1.c[0][j1] = c0
                enqueue(c1)
            }
        }
    }
}

fun main() {
    part1()
    part2()
}