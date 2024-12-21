private const val DAY = 1

fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = DAY.readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 11)

    val input = DAY.readInput("Day${DAY.toDayString()}")
    part1(input).println()
    part2(input).println()
}