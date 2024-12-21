package Day10

import println
import readInput
import toDayString

private const val DAY = 10

fun main() {
    fun part1(input: List<String>): Int {
        return input.countAllTrailheads()
    }

    fun part2(input: List<String>): Int {
        return input.countAllTrailheads2()
    }

    val testInput = DAY.readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 36)
    check(part2(testInput) == 81)

    val input = DAY.readInput("Day${DAY.toDayString()}")
    check (part1(input) == 557)
    part1(input).println()
    part2(input).println()
}

private fun List<String>.countAllTrailheads(): Int {
    var sum = 0
    getIndices().forEach { pos ->
        if (this[pos.first][pos.second].digitToInt() == 0) {
            sum += followTrailheads(
                curPos = pos,
                traversed = Array(size) { BooleanArray(this[0].length) { false } })
        }
    }
    return sum
}

private fun List<String>.followTrailheads(curPos: Pair<Int, Int>, traversed: Array<BooleanArray>): Int {
    traversed[curPos.first][curPos.second] = true
    if (this[curPos.first][curPos.second].digitToInt() == 9) {
        return 1
    }

    var sum = 0
    for (neighbors in listOf(curPos.up(), curPos.down(), curPos.right(), curPos.left())) {
        validOrNull(neighbors)?.let { nextPos ->
            if (!traversed[nextPos.first][nextPos.second]
                && this[nextPos.first][nextPos.second].digitToInt() - this[curPos.first][curPos.second].digitToInt() == 1
            ) {
                sum += followTrailheads(nextPos, traversed)
            }
        }
    }

    return sum
}

private fun List<String>.countAllTrailheads2(): Int {
    var sum = 0
    getIndices().forEach { pos ->
        if (this[pos.first][pos.second].digitToInt() == 0) {
            sum += followTrailheads2(
                curPos = pos,
                traversed = Array(size) { BooleanArray(this[0].length) { false } })
        }
    }
    return sum
}

private fun List<String>.followTrailheads2(curPos: Pair<Int, Int>, traversed: Array<BooleanArray>): Int {
    traversed[curPos.first][curPos.second] = true
    if (this[curPos.first][curPos.second].digitToInt() == 9) {
        traversed[curPos.first][curPos.second] = false
        return 1
    }

    var sum = 0
    for (neighbors in listOf(curPos.up(), curPos.down(), curPos.right(), curPos.left())) {
        validOrNull(neighbors)?.let { nextPos ->
            if (!traversed[nextPos.first][nextPos.second]
                && this[nextPos.first][nextPos.second].digitToInt() - this[curPos.first][curPos.second].digitToInt() == 1
            ) {
                sum += followTrailheads2(nextPos, traversed)
            }
        }
    }
    traversed[curPos.first][curPos.second] = false
    return sum
}

private fun List<String>.getIndices(): List<Pair<Int, Int>> {
    val set = mutableSetOf<Pair<Int, Int>>()
    for (i in indices) {
        for (j in this[0].indices) {
            set.add(Pair(i, j))
        }
    }
    return set.toList()
}

private fun List<String>.validOrNull(p: Pair<Int, Int>): Pair<Int, Int>? =
    if (p.first in indices && p.second in this[0].indices) p else null

private fun Pair<Int, Int>.up() = Pair(first - 1, second)
private fun Pair<Int, Int>.down() = Pair(first + 1, second)
private fun Pair<Int, Int>.right() = Pair(first, second + 1)
private fun Pair<Int, Int>.left() = Pair(first, second - 1)