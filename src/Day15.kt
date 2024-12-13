import java.security.InvalidParameterException

private const val DAY = 15

private typealias Grid = MutableList<MutableList<Cell15>>

private sealed class Cell15 {
    data object Empty : Cell15()
    data object Wall : Cell15()
    data object Box : Cell15()
    data object Robot : Cell15()
}

private enum class Operation(val direction: Pair<Int, Int>) {
    UP(Pair(-1, 0)),
    LEFT(Pair(0, -1)),
    RIGHT(Pair(0, +1)),
    DOWN(Pair(1, 0))
}

fun main() {
    fun part1(input: List<String>): Int {
        val (grid, operations) = input.parse()
        grid.process(operations)
        return grid.score()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testMiniInput = readInput("Day${DAY.toDayString()}_test_mini")
    check(part1(testMiniInput) == 2028)

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 10092)

    val input = readInput("Day${DAY.toDayString()}")
    part1(input).println()
    part2(input).println()
}

private fun Grid.score(): Int = indices.sumOf { i ->
    this[i].indices.sumOf { j ->
        if (this[i][j] is Cell15.Box) {
            i * 100 + j
        } else {
            0
        }
    }
}

private fun Grid.process(operations: List<Operation>) {
//    print("Initial state:")
    operations.forEach { operation ->
        val robotPos = robotPos()
        findNextFreeCell(robotPos, operation.direction)?.let { freeCell ->
            val neighborCell = robotPos+operation.direction
            this[freeCell.first][freeCell.second] = Cell15.Box
            this[neighborCell.first][neighborCell.second] = Cell15.Robot
            this[robotPos.first][robotPos.second] = Cell15.Empty
        }
//        print("Move $operation:")
    }
}

private fun Grid.findNextFreeCell(p: Pair<Int, Int>, d: Pair<Int, Int>): Pair<Int, Int>? {
    var curPos = p
    while (isValid(curPos)) {
        if (this[curPos.first][curPos.second] is Cell15.Wall) {
            return null
        }
        if (this[curPos.first][curPos.second] is Cell15.Empty) {
            return curPos
        }
        curPos += d
    }
    return null
}

private operator fun Pair<Int, Int>.plus(p: Pair<Int, Int>): Pair<Int, Int> = Pair(first + p.first, second + p.second)

private fun Grid.isValid(p: Pair<Int, Int>) = p.first in indices && p.second in this[0].indices

private fun Grid.robotPos(): Pair<Int, Int> {
    indices.forEach { i ->
        this[0].indices.forEach { j ->
            if (this[i][j] is Cell15.Robot) {
                return Pair(i, j)
            }
        }
    }
    throw InvalidParameterException()
}

private fun List<String>.parse(): Pair<Grid, List<Operation>> {
    val splitIndex = indexOfFirst { it.isBlank() }
    return Pair(this.subList(0, splitIndex).parseGrid(), this.subList(splitIndex + 1, size).parseOperations())
}

private fun List<String>.parseGrid(): Grid = map { line ->
    line.map { c ->
        when (c) {
            'O' -> Cell15.Box
            '@' -> Cell15.Robot
            '#' -> Cell15.Wall
            else -> Cell15.Empty
        }
    }.toMutableList()
}.toMutableList()

private fun List<String>.parseOperations(): List<Operation> {
    val res = mutableListOf<Operation>()

    forEachIndexed { i, r ->
        r.forEachIndexed { j, c ->
            res.add(
                when (c) {
                    '^' -> Operation.UP
                    '<' -> Operation.LEFT
                    '>' -> Operation.RIGHT
                    else -> Operation.DOWN
                }
            )
        }
    }

    return res
}

private fun Grid.print(headerMessage: String) {
    headerMessage.println()
    forEach { line ->
        line.forEach { cell ->
            kotlin.io.print(
                when (cell) {
                    Cell15.Box -> "O"
                    Cell15.Empty -> "."
                    Cell15.Robot -> "@"
                    Cell15.Wall -> "#"
                }
            )
        }
        kotlin.io.println()
    }
    kotlin.io.println()
}