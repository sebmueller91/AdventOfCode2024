private const val DAY = 7

private data class Equation(
    val result: Int,
    val numbers: List<Int>
)

fun main() {
    fun part1(input: List<String>): Int {
        return input.parse().filter { it.isValid() }.sumOf { it.result }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    println(part1(testInput))
    check(part1(testInput) == 11)

    val input = readInput("Day${DAY.toDayString()}")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.parse(): List<Equation> = this.map {
    val parts = it.split(":")
    val numbers = parts[1].split(" ")
    Equation(result = parts[0].trim().toInt(), numbers.filter { it.isNotBlank() }.map { it.trim().toInt() })
}

private fun Equation.isValid(): Boolean {

}