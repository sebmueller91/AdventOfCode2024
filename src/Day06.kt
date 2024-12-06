private const val DAY = 6

private sealed class Cell {
    data object Empty : Cell()
    data object Obstacle : Cell()
    data object Traversed : Cell()
}

fun main() {
    fun part1(input: List<String>): Int {
        val (grid, startCoord) = input.parse()
        grid.println()
        startCoord.println()
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 41)

    val input = readInput("Day${DAY.toDayString()}")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.parse(): Pair<MutableList<MutableList<Cell>>, Pair<Int, Int>> {
    lateinit var startPos: Pair<Int, Int>
    val grid = this.mapIndexed { x, it ->
        it.mapIndexed { y, it ->
            if (it == '#') {
                Cell.Obstacle
            } else if (it == '.') {
                Cell.Empty
            } else {
                startPos = Pair(x,y)
                Cell.Traversed
            }
        }.toMutableList()
    }.toMutableList()
    return Pair(grid, startPos)
}