private const val DAY = 7

private data class Equation(
    val result: Long,
    val numbers: List<Long>
)

fun main() {
    fun part1(input: List<String>): Long {
        return input.parse().filter { it.isValid() }.sumOf { it.result }
    }

    fun part2(input: List<String>): Long {
        return input.parse().filter { it.isValid() }.sumOf { it.result }
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    println(part1(testInput))
    check(part1(testInput) == 3749L)

    val input = readInput("Day${DAY.toDayString()}")
    check(part1(input) == 28730327770375L)
    part1(input).println()
    println(part2(testInput))
    check(part2(testInput) == 11387L)
    part2(input).println()
}

private fun List<String>.parse(): List<Equation> = this.map {
    val parts = it.split(":")
    val numbers = parts[1].split(" ")
    Equation(result = parts[0].trim().toLong(), numbers.filter { it.isNotBlank() }.map { it.trim().toLong() })
}

private fun Equation.isValid(): Boolean = isValidRec(0, this.numbers[0])

private fun Equation.isValidRec(pos: Int, curValue: Long): Boolean {
    if (pos + 1 !in numbers.indices) {
        return curValue == result
    }

    return isValidRec(pos + 1, curValue * numbers[pos + 1])
            || isValidRec(pos + 1, curValue + numbers[pos + 1])
}

private fun Long.concatenate(other: Long): Long = "$this$other".toLong()