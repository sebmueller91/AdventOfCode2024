private const val DAY = 11

fun main() {
    fun part1(input: List<String>): Long {
        return input.parse().applyBlinks(25)
    }

    fun part2(input: List<String>): Long {
        return input.parse().applyBlinks(75)
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    part1(testInput).println()
    check(part1(testInput) == 55312L)

    val input = readInput("Day${DAY.toDayString()}")
    check(part1(input) == 183620L)
    part1(input).println()
    part2(input).println()
}

private fun List<String>.parse(): MutableMap<Long, Long> =
    this[0]
        .split(' ')
        .mapNotNull { it.toLongOrNull() }
        .groupingBy { it }
        .eachCount()
        .mapValues { it.value.toLong() }
        .toMutableMap()

private fun MutableMap<Long, Long>.applyBlinks(n: Int): Long {
    var curMap = this
//    println("Initial setup:")
//    curMap.println()

    repeat(n) { i ->
        curMap = blink(curMap)
//                println("After ${i+1} blink:")
//        curMap.println()
    }
    return curMap.values.sumOf { it.toLong() }
}

private fun blink(map: MutableMap<Long, Long>): MutableMap<Long, Long> {
    val updatedMap = map.toMutableMap()
    for ((key, value) in map) {
        when {
            key == 0L -> {
                updatedMap[1] = updatedMap.getOrDefault(1, 0) + value
                updatedMap[key] = updatedMap.getOrDefault(0, 0) - value
            }
            key.toString().length % 2 == 0 -> {
                val (stone1, stone2) = key.splitStone()
                updatedMap[stone1] = updatedMap.getOrDefault(stone1, 0) + value
                updatedMap[stone2] = updatedMap.getOrDefault(stone2, 0) + value
                updatedMap[key] = updatedMap.getOrDefault(key, 0) - value

            }
            else -> {
                updatedMap[key * 2024] = updatedMap.getOrDefault(key * 2024, 0) + value
                updatedMap[key] = updatedMap.getOrDefault(key, 0) - value
            }
        }
    }
    return updatedMap.filter { it.value != 0L }.toMutableMap()
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
            this.add(index + 1, stone2)
            index++
        } else {
            this[index] *= 2024L
        }
        index++
    }
    return this
}

private fun Long.splitStone(): Pair<Long, Long> {
    val asString = this.toString()
    val stone1 = asString.substring(0, asString.length / 2).convertToLong()
    val stone2 = asString.substring(asString.length / 2, asString.length).convertToLong()

    return Pair(stone1, stone2)
}

private fun String.convertToLong(): Long {
    return if (this.isBlank()) 0
    else this.toLong()
}