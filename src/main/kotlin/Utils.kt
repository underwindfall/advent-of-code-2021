import java.io.File

internal fun <R> getInputAsTest(day: String, transform: String.() -> R): R =
  object {}.javaClass.getResource("input-$day.txt")?.readText()?.let { it.transform() }
    ?: error("Failed to read day $day input")

internal fun getInputAsTest(day: String): List<String> =
  File(object {}.javaClass.getResource("input-$day.txt").toURI()).readLines()
    ?: error("Failed to read day $day input")
