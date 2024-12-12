private const val DAY = 11

fun main() {
    fun part1(input: List<String>): Long {
        return input.parse().applyBlinks(25)
    }

    fun part2(input: List<String>): Long {
        return input.parse().applyBlinks(75)
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 55312L)

    val input = readInput("Day${DAY.toDayString()}")
    check(part1(input) == 183620L)
    part1(input).println()
    part2(input).println()
}

private fun List<String>.parse(): List<Long> = this[0].split(' ').map { it.convertToLong() }

private fun List<Long>.applyBlinks(n: Int): Long {
    var curList = this.toMutableList()
    for (i in 1..n) {
        curList = curList.blink()
    }
    return curList.size.toLong()
}

private fun MutableList<Long>.blink(): MutableList<Long> {
    var index = 0
    while (index < size) {
        val stone = this[index]
        if (stone == 0L) {
            this[index] = 1
        } else if (stone.toString().length % 2 == 0) {
            val (stone1, stone2) = stone.splitStone()
            this[index] = stone1
            this.add(index+1, stone2)
            index++
        } else {
            this[index] *= 2024
        }
        index++
    }
    return this
}

private fun Long.splitStone(): Pair<Long, Long> {
    val asString = this.toString()
    val stone1 = asString.substring(0,asString.length/2).convertToLong()
    val stone2 = asString.substring(asString.length/2, asString.length).convertToLong()

    return Pair(stone1, stone2)
}

private fun String.convertToLong(): Long {
    return if (this.isBlank()) 0
    else this.toLong()
}