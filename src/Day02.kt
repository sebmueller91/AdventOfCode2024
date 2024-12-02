private const val DAY = 2

fun main() {
    fun part1(input: List<String>): Int {
        return input.parse().count {
            it.checkAll { a: Int, b: Int -> a - b in 1..3 }
                    || it.checkAll { a: Int, b: Int -> b - a in 1..3 }
        }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 2)

    val input = readInput("Day${DAY.toDayString()}")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.parse() = this.map { it.split(' ').map { it.toInt() } }

private fun List<Int>.checkAll(compare: (Int, Int) -> Boolean) =
    this.zipWithNext().all { (first, second) -> compare(first, second) }
