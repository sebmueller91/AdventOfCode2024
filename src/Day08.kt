private const val DAY = 8

private data class Position(
    val isAntinode: Boolean = false,
    val cell8: Cell8 = Cell8.Empty
)

private sealed class Cell8 {
    data object Empty : Cell8()
    data class Antenna(val frequency: Char) : Cell8()
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
        return grid.fillAntinodes2(antennas).flatten().count { it.isAntinode }
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 14)

    val input = readInput("Day${DAY.toDayString()}")
    check(part1(input) == 367)
    check(part2(testInput) == 34)
    part1(input).println()
    part2(input).println()
}

private fun MutableList<MutableList<Position>>.getAntennasPositions(): Map<Char, MutableList<Pair<Int, Int>>> {
    val map = mutableMapOf<Char, MutableList<Pair<Int, Int>>>()
    for (r in indices) {
        for (c in this[0].indices) {
            val cell = this[r][c].cell8
            if (cell is Cell8.Antenna) {
                map.getOrPut(cell.frequency) { mutableListOf() }
                    .add(Pair(r, c))
            }
        }
    }
    return map
}

private fun MutableList<MutableList<Position>>.printGrid() {
    for (row in this) {
        for (position in row) {
            when {
                position.cell8 is Cell8.Antenna && position.isAntinode -> print("!")
                position.isAntinode -> print("#")
                position.cell8 is Cell8.Antenna -> print(position.cell8.frequency)
                else -> print(".")
            }
        }
        kotlin.io.println()
    }
}

private fun MutableList<MutableList<Position>>.fillAntinodes(antennas: Map<Char, MutableList<Pair<Int, Int>>>): MutableList<MutableList<Position>> {
    antennas.forEach { (_, positions) ->
        positions.forEachIndexed { i1, p1 ->
            for (i2 in i1 + 1 until positions.size) {
                val (r1, c1) = p1
                val (r2, c2) = positions[i2]

                val dr = r2 - r1
                val dc = c2 - c1

                markAntinode(Pair(r1 - dr, c1 - dc))
                markAntinode(Pair(r2 + dr, c2 + dc))
            }
        }
    }
    return this
}

private fun MutableList<MutableList<Position>>.fillAntinodes2(antennas: Map<Char, MutableList<Pair<Int, Int>>>): MutableList<MutableList<Position>> {
    antennas.forEach { (_, positions) ->
        positions.forEachIndexed { i1, a1 ->
            for (i2 in i1 + 1 until positions.size) {
                val a2 = positions[i2]

                val (r1, c1) = a1
                val (r2, c2) = a2

                val dr = r2 - r1
                val dc = c2 - c1

                markAllAntinodes(a1, a2, Pair(dr, dc))
            }
        }
    }
    return this
}

private fun MutableList<MutableList<Position>>.markAntinode(pos: Pair<Int, Int>) {
    val (r, c) = pos

    if (isValid(pos)) {
        this[r][c] = this[r][c].copy(isAntinode = true)
    }
}

private fun MutableList<MutableList<Position>>.markAllAntinodes(
    a1: Pair<Int, Int>,
    a2: Pair<Int, Int>,
    d: Pair<Int, Int>
) {
    var p = a1
    while (isValid(p)) {
        markAntinode(p)
        p = Pair(p.first + d.first, p.second + d.second)
    }

    p = a1
    while (isValid(p)) {
        markAntinode(p)
        p = Pair(p.first - d.first, p.second - d.second)
    }

}

private fun MutableList<MutableList<Position>>.isValid(p: Pair<Int, Int>): Boolean =
    p.first in indices && p.second in this[0].indices

private fun List<String>.parse(): MutableList<MutableList<Position>> = map {
    it.map { char ->
        when (char) {
            '.' -> {
                Position()
            }

            else -> {
                Position(cell8 = Cell8.Antenna(char))
            }
        }
    }.toMutableList()
}.toMutableList()