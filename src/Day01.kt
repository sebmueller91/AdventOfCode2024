private const val DAY = 1

fun main() {
    fun part1(input: List<String>): Int {
        val (leftList, rightList) = getSortedLists(input)

        var diff = 0
        leftList.forEachIndexed { index, leftEntry ->
            val rightEntry = rightList[index]
            diff += Math.abs(leftEntry - rightEntry)
        }
        return diff
    }

    fun part2(input: List<String>): Int {
        val (leftList, rightList) = getSortedLists(input)

        var similarity = 0
        leftList.forEachIndexed { index, leftEntry ->
            similarity += leftEntry * rightList.count { it == leftEntry }
        }
        return similarity
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 11)

    val input = readInput("Day${DAY.toDayString()}")
    part1(input).println()
    part2(input).println()
}

private fun getSortedLists(input: List<String>): Pair<List<Int>, List<Int>> {
    val leftList = mutableListOf<String>()
    val rightList = mutableListOf<String>()

    input.forEach { line ->
        val parts = line.split(' ').filter { it.isNotBlank() }
        leftList.add(parts[0])
        rightList.add(parts[1])
    }

    leftList.sort()
    rightList.sort()

    val leftListSorted = leftList.map { it.toInt() }
    val rightListSorted = rightList.map { it.toInt() }

    return Pair(leftListSorted, rightListSorted)
}