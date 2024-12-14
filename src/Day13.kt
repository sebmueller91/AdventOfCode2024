private const val DAY = 13

private data class Equation(
    val xa: Long,
    val ya: Long,
    val xb: Long,
    val yb: Long,
    val xp: Long,
    val yp: Long,
)

fun main() {
    fun part1(input: List<String>): Long {
        return input.parse().mapNotNull { it.solve() }.sumOf { it.first * 3 + it.second }
    }

    fun part2(input: List<String>): Long {
        return input.parse().map { it.copy(xp = it.xp + 10000000000000L, yp = it.yp + 10000000000000L) }
            .mapNotNull { it.solve() }.sumOf { it.first * 3 + it.second }
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    part1(testInput).println()
    check(part1(testInput) == 480L)

    val input = readInput("Day${DAY.toDayString()}")
    check(part1(input) == 37901L)
    part1(input).println()
    part2(input).println()
}

private fun List<String>.parse(): List<Equation> {
    val equations = mutableListOf<Equation>()
    val abRegex = Regex("""Button [AB]: X\+(\d+), Y\+(\d+)""")
    val pRegex = Regex("""Prize: X=(\d+), Y=(\d+)""")

    for (i in 0..indices.last step 4) {
        val a = this[i].matchXandY(abRegex)
        val b = this[i + 1].matchXandY(abRegex)
        val p = this[i + 2].matchXandY(pRegex)
        equations.add(
            Equation(
                xa = a.first,
                ya = a.second,
                xb = b.first,
                yb = b.second,
                xp = p.first,
                yp = p.second
            )
        )
    }

    return equations
}

private fun Equation.solve(): Pair<Long, Long>? {
    val d = xa * yb - ya * xb
    val dx = xp * yb - yp * xb
    val dy = xa * yp - ya * xp

    if (d == 0L) return null
    val res = Pair(dx / d, dy / d)
    if (res.first < 0 || res.second < 0) return null
    if (res.first * xa + res.second * xb != xp) return null
    if (res.first * ya + res.second * yb != yp) return null

    return res
}

private fun String.matchXandY(regex: Regex): Pair<Long, Long> {
    val matchResult = regex.find(this)
    return Pair(matchResult!!.groupValues[1].toLong(), matchResult.groupValues[2].toLong())
}