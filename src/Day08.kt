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
        val grid = input.parse()
        val antennas = grid.getAntennasPositions()
        return grid.fillAntinodes(antennas).flatten().count { it.isAntinode }
    }

    fun part2(input: List<String>): Int {
        val grid = input.parse()
        val antennas = grid.getAntennasPositions()
        return grid.fillAntinodes(antennas).flatten().count { it.isAntinode }
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    part1(testInput).println()
    check(part1(testInput) == 14)

    val input = readInput("Day${DAY.toDayString()}")
    part1(input).println()
    check(part1(input) == 367)
    check (part2(testInput) == 34)
    part1(input).println()
    part2(input).println()
}

private fun MutableList<MutableList<Position>>.getAntennasPositions(): Map<Char, MutableList<Pair<Int, Int>>> {
    val map = mutableMapOf<Char, MutableList<Pair<Int, Int>>>()
    for (r in indices) {
        for (c in this[0].indices) {
            val cell = this[r][c].cell
            if (cell is Cell.Antenna) {
                map.getOrPut(cell.frequency) { mutableListOf() }
                    .add(Pair(r,c))
            }
        }
    }
    return map
}

private fun MutableList<MutableList<Position>>.printGrid() {
    for (row in this) {
        for (position in row) {
            when {
                position.cell is Cell.Antenna && position.isAntinode -> print("!")
                position.isAntinode -> print("#")
                position.cell is Cell.Antenna -> print(position.cell.frequency)
                else -> print(".")
            }
        }
        kotlin.io.println()
    }
}

private fun MutableList<MutableList<Position>>.fillAntinodes(antennas: Map<Char, MutableList<Pair<Int, Int>>>): MutableList<MutableList<Position>> {
    antennas.forEach { (_, positions) ->
        positions.forEachIndexed { i1, p1 ->
            for (i2 in i1+1 until positions.size) {
                val (r1, c1) = p1
                val (r2, c2) = positions[i2]

                val dr = r2 - r1
                val dc = c2 - c1

                markAntinode(r1-dr, c1 - dc)
                markAntinode(r2+dr, c2 + dc)
            }
        }
    }
    return this
}

private fun MutableList<MutableList<Position>>.markAntinode(row: Int, col: Int) {
    if (row in indices && col in this[0].indices) {
        this[row][col] = this[row][col].copy(isAntinode = true)
    }
}

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