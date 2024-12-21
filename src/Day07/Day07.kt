package Day07

import println
import readInput
import toDayString

private const val DAY = 7

private data class Equation7(
    val result: Long,
    val numbers: List<Long>
)

fun main() {
    fun part1(input: List<String>): Long {
        return input.parse().filter { it.isValid() }.sumOf { it.result }
    }

    fun part2(input: List<String>): Long {
        return input.parse().filter { it.isValid(true) }.sumOf { it.result }
    }

    val testInput = DAY.readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 3749L)

    val input = DAY.readInput("Day${DAY.toDayString()}")
    check(part1(input) == 28730327770375L)
    part1(input).println()
    check(part2(testInput) == 11387L)
    part2(input).println()
}

private fun List<String>.parse(): List<Equation7> = this.map {
    val parts = it.split(":")
    val numbers = parts[1].split(" ")
    Equation7(result = parts[0].trim().toLong(), numbers.filter { it.isNotBlank() }.map { it.trim().toLong() })
}

private fun Equation7.isValid(useConcat: Boolean = false): Boolean = isValidRec(0, this.numbers[0], useConcat)

private fun Equation7.isValidRec(pos: Int, curValue: Long, useConcat: Boolean): Boolean {
    if (pos + 1 !in numbers.indices) {
        return curValue == result
    }

    return isValidRec(pos + 1, curValue * numbers[pos + 1], useConcat)
            || isValidRec(pos + 1, curValue + numbers[pos + 1], useConcat)
            || useConcat && isValidRec(pos + 1, curValue.concat(numbers[pos+1]), useConcat)
}

private fun Long.concat(other: Long): Long = "$this$other".toLong()