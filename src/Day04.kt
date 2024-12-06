private const val DAY = 4

private enum class Direction(val vector: Pair<Int, Int>) {
    DOWN(Pair(1, 0)),
    RIGHT(Pair(0, 1)),
    DIAGONAL_RIGHT_DOWN(Pair(1,1)),
    DIAGONAL_RIGHT_UP(Pair(-1,1)),
}

private const val WORD = "XMAS"

fun main() {
    fun part1(input: List<String>): Int {
        return input.format().traverseAll()
    }

    fun part2(input: List<String>): Int {
        return input.format().checkAllForMatch()
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 18)

    val input = readInput("Day${DAY.toDayString()}")
    check(part1(input) == 2557)
    part1(input).println()
    part2(input).println()
}

private fun List<String>.format() = this.map { it.toCharArray() }.toTypedArray()

private fun Array<CharArray>.getBoundaryIndices(
    top: Boolean = false,
    right: Boolean = false,
    bottom: Boolean = false,
    left: Boolean = false,
): List<Pair<Int, Int>> {
    val rows = this.size
    val cols = this[0].size

    val indices = mutableSetOf<Pair<Int, Int>>()
    if (top) {
        indices.addAll((0 until cols).map { col -> Pair(0, col) })
    }
    if (right) {
        indices.addAll((0 until rows).map { row -> Pair(row, cols - 1) })
    }
    if (bottom) {
        indices.addAll((0 until cols).map { col -> Pair(rows - 1, col) })
    }
    if (left) {
        indices.addAll((0 until rows).map { row -> Pair(row, 0) })
    }
    return indices.toList()
}

private fun Array<CharArray>.traversePath(startCoord: Pair<Int, Int>, direction: Direction): Int {
    val extractedElements = mutableListOf<Char>()
    var row = startCoord.first
    var col = startCoord.second

    while (row in indices && col in 0 until this[0].size) {
        extractedElements.add(this[row][col])
        row += direction.vector.first
        col += direction.vector.second
    }
    val extractedString = extractedElements.joinToString("")

    return extractedString.countOccurences() + extractedString.reversed().countOccurences()
}

private fun Array<CharArray>.traverseAll(): Int {
    var count = 0
    getBoundaryIndices(top = true).forEach { index ->
        count += traversePath(startCoord = index, direction = Direction.DOWN)
    }
    getBoundaryIndices(left = true).forEach { index ->
        count += traversePath(startCoord = index, direction = Direction.RIGHT)
    }
    getBoundaryIndices(left = true, bottom = true).forEach { index ->
        count += traversePath(startCoord = index, direction = Direction.DIAGONAL_RIGHT_UP)
    }
    getBoundaryIndices(left = true, top = true).forEach { index ->
        count += traversePath(startCoord = index, direction = Direction.DIAGONAL_RIGHT_DOWN)
    }

    return count
}

private fun String.countOccurences(): Int {
    val regex = Regex("XMAS")
    val matches = regex.findAll(this)

    return matches.count()
}

private fun Array<CharArray>.checkAllForMatch(): Int {
    var count = 0
    for (x in indices.drop(1).dropLast(1)) {
        for (y in 1..<this[0].size-1) {
            count += if (isXmasMatch(x, y)) 1 else 0
        }
    }
    return count
}

private fun Array<CharArray>.isXmasMatch(x: Int, y: Int): Boolean {
    if (this[x][y] != 'A') {
        return false
    }

    val patterns = listOf("MMSS","SMMS","SSMM", "MSSM")
    for (p in patterns) {
        if (this[x-1][y-1] == p[0] && this[x-1][y+1] == p[1] &&
            this[x+1][y+1] == p[2] && this[x+1][y-1] == p[3]) {
            return true
        }
    }
    return  false
}