package Day16

import println
import readInput
import toDayString
import java.security.InvalidParameterException
import java.util.PriorityQueue

private const val DAY = 16

private typealias Maze = List<List<Cell>>

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

private data class DijkstraState(
    val pos: Coordinate,
    val score: Int,
    val direction: Direction
)

private sealed class Cell {
    data object Wall : Cell()
    data object Start : Cell()
    data object End : Cell()
    data object Empty : Cell()
}

fun main() {
    fun part1(input: List<String>): Int {
        val startPos = input.find('S')
        val endPos = input.find('E')
        val maze = input.parse()
        return maze.dijkstaFill(startPos, endPos, Direction.RIGHT)
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = DAY.readInput("Day${DAY.toDayString()}_test")
    part1(testInput).println()
    check(part1(testInput) == 11048)

    val input = DAY.readInput("Day${DAY.toDayString()}")
    part1(input).println()
    part2(input).println()
}

private fun Maze.dijkstaFill(
    startPos: Coordinate,
    endPos: Coordinate,
    startDirection: Direction
): Int {
    val distances = mutableMapOf<Coordinate, Int>().withDefault { Int.MAX_VALUE }
    val prioritoyQueue = PriorityQueue(compareBy<DijkstraState> { it.score })

    prioritoyQueue.add(DijkstraState(startPos, 0, startDirection))
    distances[startPos] = 0

    while (prioritoyQueue.isNotEmpty()) {
        val state = prioritoyQueue.poll()
        if (state.pos == endPos) {
            return state.score
        }

        addIfBetter(state.copy(pos = state.pos + state.direction, score = state.score + 1, direction = state.direction), distances, prioritoyQueue)
        addIfBetter(state.copy(pos = state.pos + state.direction.left(), score = state.score + 1001, direction = state.direction.left()), distances, prioritoyQueue)
        addIfBetter(state.copy(pos = state.pos + state.direction.right(), score = state.score + 1001, direction = state.direction.right()), distances, prioritoyQueue)
    }
    return Int.MAX_VALUE
}


private fun Maze.addIfBetter(state: DijkstraState, distances: MutableMap<Coordinate, Int>, queue: PriorityQueue<DijkstraState>) {
    getIfValid(state.pos)?.let {
        if (distances.getValue(state.pos) > state.score) {
            distances[state.pos] = state.score
            queue.add(state)
        }
    }
}

private fun Maze.dijkstaFillRec(
    curPos: Coordinate,
    curScore: Int,
    curDirection: Direction,
    distances: MutableList<MutableList<Int>>
) {
    if (distances[curPos.x][curPos.y] <= curScore) {
        return
    }

    distances[curPos.x][curPos.y] = curScore

    if (get(curPos) is Cell.End) {
        return return
    }

    dijkstaFillRec(curPos + curDirection, curScore + 1, curDirection, distances)
    dijkstaFillRec(curPos + curDirection.left(), curScore + 1001, curDirection.left(), distances)
    dijkstaFillRec(curPos + curDirection.right(), curScore + 1001, curDirection.right(), distances)
}

private fun List<String>.parse(): Maze = map { line ->
    line.map {
        when (it) {
            'E' -> Cell.End
            'S' -> Cell.Start
            '#' -> Cell.Wall
            else -> Cell.Empty
        }
    }
}

private fun Maze.get(coordinate: Coordinate) = this[coordinate.x][coordinate.y]
private fun Maze.getIfValid(coordinate: Coordinate) = if (isValid(coordinate) && get(coordinate) !is Cell.Wall) coordinate else null
private fun Maze.isValid(coordinate: Coordinate) = coordinate.x in indices && coordinate.y in this[0].indices

private fun List<String>.find(char: Char): Coordinate {
    indices.forEach { i ->
        this[0].indices.forEach { j ->
            if (this[i][j] == char) {
                return Coordinate(i, j)
            }
        }
    }
    throw InvalidParameterException()
}