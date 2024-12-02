private const val DAY = 2

fun main() {
    fun part1(input: List<String>): Int {
        return input.parse().count { it.checkLevel() }
    }

    fun part2(input: List<String>): Int {
        return input.parse().count {
            it.checkLevelWithMismatch()
        }
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 2)

    val input = readInput("Day${DAY.toDayString()}")
    check(part1(input) == 359)
    part1(input).println()
    part2(input).println()
}

private fun List<String>.parse() = this.map { it.split(' ').map { it.toInt() } }

private fun Pair<Int, Int>.increasing() = this.second - this.first in 1..3
private fun Pair<Int, Int>.decreasing() = this.first - this.second in 1..3

private fun List<Int>.levelIncreasing() =
    this.zipWithNext().all { it.increasing() }

private fun List<Int>.levelDecreasing() =
    this.zipWithNext().all { it.decreasing() }

private fun List<Int>.checkLevel() =
    this.levelIncreasing() || levelDecreasing()

private fun List<Int>.checkLevelWithMismatch(): Boolean =
    this.indices.any { index ->
        val newList = this.toMutableList().apply { removeAt(index) }
        newList.checkLevel()
    }
