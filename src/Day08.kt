private const val DAY = 8

private data class Position(
    val isAntinode: Boolean = false,
    val cell: Cell = Cell.Empty
)

private sealed class Cell {
    data object Empty : Cell()
    data class Antenna(val frequency: Char) : Cell()
}

fun main() {
    fun part1(input: List<String>): Int {
        return input.parse().fillAntinodes().flatten().count { it.isAntinode }
    }

    fun part2(input: List<String>): Int {
        return input.parse().fillAntinodes().flatten().count { it.isAntinode }
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    part1(testInput).println()
    check(part1(testInput) == 14)

    val input = readInput("Day${DAY.toDayString()}")
    check(part1(input) == 367)
    check (part2(testInput) == 34)
    part1(input).println()
    part2(input).println()
}

private fun MutableList<MutableList<Position>>.fillAntinodes(): MutableList<MutableList<Position>> {
    forEachIndexed { x, row ->
        row.forEachIndexed { y, _ ->
            if (isAntinode1(x, y)) {
                this[x][y] = this[x][y].copy(isAntinode = true)
            }
        }
    }
    return this
}

private fun MutableList<MutableList<Position>>.isAntinode1(row: Int, col: Int): Boolean {
    for (r1 in indices) {
        for (c1 in this[0].indices) {
            if (r1 == row && c1 == col) {
                continue
            }
            val (r2, c2) = Pair(row + 2 * (r1 - row), col + 2 * (c1 - col))

            val pos1 = this[r1][c1]
            val pos2 = this.getOrNull(r2, c2) ?: continue

            if (pos1.cell is Cell.Antenna
                && pos2.cell is Cell.Antenna
                && pos1.cell.frequency == pos2.cell.frequency
            ) {
                return true
            }
        }
    }
    return false
}

private fun MutableList<MutableList<Position>>.isAntinode2(row: Int, col: Int): Boolean {
    for (r1 in indices) {
        for (c1 in this[0].indices) {
            if (r1 == row && c1 == col) {
                continue
            }
            val (r2, c2) = Pair(row + 2 * (r1 - row), col + 2 * (c1 - col))

            val pos1 = this[r1][c1]
            val pos2 = this.getOrNull(r2, c2) ?: continue

            if (pos1.cell is Cell.Antenna
                && pos2.cell is Cell.Antenna
                && pos1.cell.frequency == pos2.cell.frequency
            ) {
                return true
            }
        }
    }
    return false
}

private fun MutableList<MutableList<Position>>.getOrNull(row: Int, col: Int): Position? =
    if (row in indices && col in this[0].indices) this[row][col] else null

private fun List<String>.parse(): MutableList<MutableList<Position>> = map {
    it.map { char ->
        when (char) {
            '.' -> {
                Position()
            }

            else -> {
                Position(cell = Cell.Antenna(char))
            }
        }
    }.toMutableList()
}.toMutableList()