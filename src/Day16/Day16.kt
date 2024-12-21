package Day16

import println
import readInput
import toDayString
import java.security.InvalidParameterException
import kotlin.math.min

private const val DAY = 16

private typealias Maze = List<List<Cell>>

private const val ALMOST_MAX_VALUE = Int.MAX_VALUE-100

private enum class Direction(val vector: Coordinate) {
    RIGHT(Coordinate(0, 1)),
    DOWN(Coordinate(1, 0)),
    LEFT(Coordinate(0, -1)),
    UP(Coordinate(-1, 0));

    fun left() = when (this) {
        RIGHT -> UP
        DOWN -> RIGHT
        LEFT -> DOWN
        UP -> LEFT
    }

    fun right() = when (this) {
        RIGHT -> DOWN
        DOWN -> LEFT
        LEFT -> UP
        UP -> RIGHT
    }
}

private data class Coordinate(
    val x: Int,
    val y: Int
) {
    operator fun plus(direction: Direction): Coordinate =
        Coordinate(this.x + direction.vector.x, this.y + direction.vector.y)
}

private sealed class Cell {
    data object Wall : Cell()
    data object End : Cell()
    data object Empty : Cell()
}

fun main() {
    fun part1(input: List<String>): Int {
        val startPos = input.findStart()
        val maze = input.parse()
        return maze.findShortesWay(startPos, listOf(), 0, Direction.RIGHT)
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    part1(testInput).println()
    check(part1(testInput) == 11048)

    val input = readInput("Day${DAY.toDayString()}")
    part1(input).println()
    part2(input).println()
}

private var curBestResult = Int.MAX_VALUE

private fun Maze.findShortesWay(
    curPos: Coordinate,
    traversed: List<Coordinate>,
    curScore: Int,
    curDirection: Direction
): Int {
    if (get(curPos) is Cell.End) {
        return curScore
    }

    curBestResult = min(curBestResult, traverseIfValid(curPos + curDirection, traversed, curScore + 1, curDirection))
    curBestResult = min(curBestResult, traverseIfValid(curPos + curDirection.left(), traversed, curScore + 1001, curDirection.left()))
    curBestResult = min(curBestResult, traverseIfValid(curPos + curDirection.right(), traversed, curScore + 1001, curDirection.right()))

    return curBestResult
}

private fun Maze.traverseIfValid(
    nextPos: Coordinate,
    traversed: List<Coordinate>,
    score: Int,
    curDirection: Direction
): Int {
    getOrNull(nextPos)?.let { nextPos ->
        if (isValid(nextPos) && get(nextPos) !is Cell.Wall && !traversed.contains(nextPos)) {
            return findShortesWay(
                nextPos,
                traversed.toMutableList().apply { add(nextPos) },
                score,
                curDirection
            )
        }
    }

    return Int.MAX_VALUE
}

private fun List<String>.parse(): Maze = map { line ->
    line.map {
        when (it) {
            'E' -> Cell.End
            '#' -> Cell.Wall
            else -> Cell.Empty
        }
    }
}

private fun Maze.get(coordinate: Coordinate) = this[coordinate.x][coordinate.y]
private fun Maze.getOrNull(coordinate: Coordinate) = if (isValid(coordinate)) coordinate else null
private fun Maze.isValid(coordinate: Coordinate) = coordinate.x in indices && coordinate.y in this[0].indices

private fun List<String>.findStart(): Coordinate {
    indices.forEach { i ->
        this[0].indices.forEach { j ->
            if (this[i][j] == 'S') {
                return Coordinate(i, j)
            }
        }
    }
    throw InvalidParameterException()
}