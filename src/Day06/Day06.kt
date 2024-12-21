package Day06

import println
import readInput
import toDayString

private const val DAY = 6

private sealed class Cell {
    data object Empty : Cell()
    data object Obstacle : Cell()
    data object Traversed : Cell()
}

private object DirectionVector {
    private var index = 0
    private val vectors = listOf(Pair(-1, 0), Pair(0, 1), Pair(1, 0), Pair(0, -1))

    fun reset() {
        index = 0
    }

    fun get(): Pair<Int, Int> = vectors[index]
    fun getIndex() = index
    fun turnRight() {
        index = (index + 1) % 4
    }

    fun print() {
        print(
            when (index) {
                0 -> "^"
                1 -> ">"
                2 -> "v"
                else -> "<"
            }
        )
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        DirectionVector.reset()
        val (grid, startPos) = input.parse()
        return grid.walk(startPos).countTraversedCells()
    }

    fun part2(input: List<String>): Int {
        DirectionVector.reset()
        val (grid, startPos) = input.parse()
        return grid.countObstacleLoops(startPos)
    }

    val testInput = DAY.readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    val input = DAY.readInput("Day${DAY.toDayString()}")
    check(part1(input) == 4454)
    part1(input).println()
    part2(input).println()
}

private fun MutableList<MutableList<Cell>>.walk(startPos: Pair<Int, Int>): List<MutableList<Cell>> {
    var curPos = startPos

    while (true) {
        this[curPos.first][curPos.second] = Cell.Traversed

        val nextPos = getNextPos(curPos)
        if (!this.checkIndex(nextPos)) {
            break
        }
        if (this[nextPos.first][nextPos.second] == Cell.Obstacle) {
            DirectionVector.turnRight()

        } else {
            curPos = nextPos
        }
    }

    return this
}

private fun MutableList<MutableList<Cell>>.isLoop(startPos: Pair<Int, Int>): Boolean {
    var curPos = startPos
    val hasBeenTraversedInSameDirection =
        MutableList(this.size) { MutableList(this[0].size) { MutableList(4) {false} } }

    while (true) {
        if (hasBeenTraversedInSameDirection[curPos.first][curPos.second][DirectionVector.getIndex()]) {
            return true
        }
        hasBeenTraversedInSameDirection[curPos.first][curPos.second][DirectionVector.getIndex()] = true

        val nextPos = getNextPos(curPos)
        if (!this.checkIndex(nextPos)) {
            break
        }
        if (this[nextPos.first][nextPos.second] == Cell.Obstacle) {
            DirectionVector.turnRight()

        } else {
            curPos = nextPos
        }
    }

    return false
}

private fun MutableList<MutableList<Cell>>.checkIndex(pos: Pair<Int, Int>): Boolean =
    pos.first in indices && pos.second in this[0].indices

private fun getNextPos(curPos: Pair<Int, Int>): Pair<Int, Int> =
    Pair(curPos.first + DirectionVector.get().first, curPos.second + DirectionVector.get().second)

private fun List<List<Cell>>.countTraversedCells(): Int = this.flatten().count { it == Cell.Traversed }

private fun List<List<Cell>>.printGrid(curPos: Pair<Int, Int>? = null) {
    print(" ")
    println(this.indices.joinToString(""))
    this.forEachIndexed { x, row ->
        print(x)
        row.forEachIndexed { y, cell ->

            if (curPos?.first == x && curPos.second == y) {
                DirectionVector.print()
            } else {
                print(
                    when (cell) {
                        Cell.Empty -> "."
                        Cell.Obstacle -> "#"
                        Cell.Traversed -> "X"
                    }
                )
            }

        }
        kotlin.io.println()
    }
    kotlin.io.println()
}

private fun List<String>.parse(): Pair<MutableList<MutableList<Cell>>, Pair<Int, Int>> {
    lateinit var startPos: Pair<Int, Int>
    val grid = this.mapIndexed { x, row ->
        row.mapIndexed { y, cell ->
            when (cell) {
                '#' -> {
                    Cell.Obstacle
                }

                '.' -> {
                    Cell.Empty
                }

                else -> {
                    startPos = Pair(x, y)
                    Cell.Empty
                }
            }
        }.toMutableList()
    }.toMutableList()
    return Pair(grid, startPos)
}

private fun MutableList<MutableList<Cell>>.countObstacleLoops(startPos: Pair<Int, Int>): Int {
    var count = 0

    this.forEachIndexed { x, row ->
        row.forEachIndexed { y, cell ->
            if (cell is Cell.Empty) {
                DirectionVector.reset()
                this[x][y] = Cell.Obstacle
                if (isLoop(startPos)) {
                    count++
                }
                this[x][y] = Cell.Empty
            }
        }
    }

    return count
}