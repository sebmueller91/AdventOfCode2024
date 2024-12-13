private const val DAY = 12

private enum class Direction(val vector: Pair<Int, Int>) {
    RIGHT(Pair(0, 1)),
    DOWN(Pair(1, 0)),
    LEFT(Pair(0, -1)),
    UP(Pair(-1, 0));
}

fun main() {
    fun part1(input: List<String>): Int {
        return input.measure().sumOf { it.first * it.second }
    }

    fun part2(input: List<String>): Int {
        return input.countCorners().sumOf { it.first * it.second }
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 1930)
    check(part2(testInput) == 1206)

    val input = readInput("Day${DAY.toDayString()}")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.measure(): List<Pair<Int, Int>> {
    val regions = mutableListOf<Pair<Int, Int>>()
    val visited = Array(size) { BooleanArray(this[0].length) { false } }

    indices.forEach { i ->
        this[i].indices.forEach { j ->
            if (!visited[i][j]) {
                regions.add(measure(Pair(i, j), visited))
            }
        }
    }

    return regions
}

private fun List<String>.measure(pos: Pair<Int, Int>, visited: Array<BooleanArray>): Pair<Int, Int> {
    visited[pos.first][pos.second] = true
    var area = 1
    var perimeter = 0

    Direction.entries.forEach { direction: Direction ->
        val neighborPos = pos.plus(direction.vector)
        val neighbor = getOrNull(neighborPos)
        when {
            neighbor == null -> {
                perimeter++
            }

            neighbor != this[pos.first][pos.second] -> {
                perimeter++
            }

            !visited[neighborPos.first][neighborPos.second] -> {
                val (a, p) = measure(neighborPos, visited)
                area += a
                perimeter += p
            }
        }
    }

    return Pair(area, perimeter)
}


private fun List<String>.countCorners(): List<Pair<Int, Int>> {
    val regions = mutableListOf<Pair<Int, Int>>()
    val visited = Array(size) { BooleanArray(this[0].length) { false } }

    indices.forEach { i ->
        this[i].indices.forEach { j ->
            if (!visited[i][j]) {
                regions.add(countCorners(Pair(i, j), visited))
            }
        }
    }

    return regions
}

private fun List<String>.countCorners(pos: Pair<Int, Int>, visited: Array<BooleanArray>): Pair<Int, Int> {
    visited[pos.first][pos.second] = true
    var area = 1
    var corners = 0
    corners += getNumberCorners(pos)
    Direction.entries.forEach { direction: Direction ->
        val neighborPos = pos.plus(direction.vector)
        val neighbor = getOrNull(neighborPos)
        if (neighbor == this[pos.first][pos.second] && !visited[neighborPos.first][neighborPos.second]) {
            val (a, c) = countCorners(neighborPos, visited)
            area += a
            corners += c
        }
    }

    return Pair(area, corners)
}

private fun List<String>.getNumberCorners(pos: Pair<Int, Int>): Int {
    val currentValue = this[pos.first][pos.second]
    var corners = 0

    listOf(
        Triple(Direction.UP, Direction.LEFT, pos.plus(Direction.UP.vector).plus(Direction.LEFT.vector)),
        Triple(Direction.UP, Direction.RIGHT, pos.plus(Direction.UP.vector).plus(Direction.RIGHT.vector)),
        Triple(Direction.DOWN, Direction.LEFT, pos.plus(Direction.DOWN.vector).plus(Direction.LEFT.vector)),
        Triple(Direction.DOWN, Direction.RIGHT, pos.plus(Direction.DOWN.vector).plus(Direction.RIGHT.vector))
    ).forEach { (d1, d2, d3) ->
        val neighbor1 = getOrNull(pos.plus(d1.vector))
        val neighbor2 = getOrNull(pos.plus(d2.vector))
        val diagonalNeighbor = getOrNull(d3)

        if ((neighbor1 != currentValue && neighbor2 != currentValue) ||
            (neighbor1 == currentValue && neighbor2 == currentValue && diagonalNeighbor != currentValue)
        ) {
            corners++
        }
    }
    return corners
}


private fun Pair<Int, Int>.plus(p: Pair<Int, Int>) = Pair(this.first + p.first, this.second + p.second)

private fun List<String>.getOrNull(pos: Pair<Int, Int>): Char? =
    if (pos.first in indices && pos.second in this[0].indices) this[pos.first][pos.second] else null

private fun List<String>.isValid(pos: Pair<Int, Int>): Boolean =
    pos.first in indices && pos.second in this[0].indices