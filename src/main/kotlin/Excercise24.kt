val inputList = getInputAsTest("24")

private fun solve(range: IntProgression): Long {
    val cycleSize = inputList.drop(1).withIndex().first { it.value.startsWith("inp") }.index + 1
    val parts = inputList.chunked(cycleSize).map { Values(it) }

    for (input in range) {
        var (z, digitPosition, submarineNumber) = Triple(0, 0, "")
        for (part in parts) {
            val digit: Int
            if (part.div == 1) {
                digit = input.toString().padStart(7, '0')[digitPosition].toString().toInt()
                digitPosition++
            } else
                digit = z % 26 + part.check
            z = part.computeZ(digit, z)
            submarineNumber += digit
        }

        if (validNumber(z, submarineNumber))
            return submarineNumber.toLong()
    }

    return 0
}

private fun String.firstInt(includeNegative: Boolean = true) = allInts(includeNegative).first()
private fun String.allInts(includeNegative: Boolean = true): List<Int> =
    (if (includeNegative) "(-?\\d+)" else "(\\d+)").toRegex().findAll(this).map { it.value.toInt() }.toList()

private fun validNumber(z: Int, number: String) =
    z == 0 && number.length == 14 && !number.contains('0')

/** Returns the string without the last character. */
private fun String.butLast() = substring(0, length - 1)

private inline fun <T> List<T>.second(predicate: (T) -> Boolean): T {
    return filter { predicate(it) }[1]
}


data class Values(val div: Int, val check: Int, val offset: Int) {
    constructor(instructions: Instructions) :
            this(
                instructions.first { it.startsWith("div") }.firstInt(),  // first DIV
                instructions.second { it.startsWith("add") }.firstInt(), // second ADD
                instructions.filter { it.startsWith("add") }.dropLast(1).last().firstInt() // before last ADD
            )

    fun computeZ(w: Int, z: Int): Int {
        val x = if ((z % 26) + check != w) 1 else 0
        return (z / div) * (25 * x + 1) + ((w + offset) * x)
    }
}

private fun partOne() = solve(9999999 downTo 1111111)

// should start at 1111111, but speed it up starting near the correct answer
private fun partTwo() = solve(2222222..9999999)

fun main() {
    println("partOne() ${partOne()}")
    println("partTwo() ${partTwo()}")
}

private typealias Instructions = List<String>