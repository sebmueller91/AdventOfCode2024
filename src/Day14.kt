import org.w3c.dom.ranges.Range

private const val DAY = 14

private data class Robot(
    val p: Pair<Int, Int>,
    val v: Pair<Int, Int>
)

fun main() {
    fun part1(input: List<String>, size: Pair<Int, Int>): Int {
        return simulate(input.parse(), 100, size).mulQuadrants(size)
    }

    fun part2(input: List<String>, size: Pair<Int, Int>): Int {
        return simulate(input.parse(), 6476, size).mulQuadrants(size)
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    part1(testInput, Pair(11, 7)).println()
    check(part1(testInput, Pair(11, 7)) == 12)

    val input = readInput("Day${DAY.toDayString()}")
    part1(input, Pair(101, 103)).println()
    part2(input, Pair(101, 103)).println()
}

private fun List<Robot>.mulQuadrants(size: Pair<Int, Int>): Int {
    val q1x = 0.until(size.first / 2)
    val q1y = 0.until(size.second / 2)

    val q2x = 0.until(size.first / 2)
    val q2y = (size.second / 2 + 1)..<size.second

    val q3x = (size.first / 2 + 1)..<size.first
    val q3y = 0.until(size.second / 2)

    val q4x = (size.first / 2 + 1)..<size.first
    val q4y = (size.second / 2 + 1)..<size.second

    return getNumberRobotsInQuadrant(q1x, q1y) * getNumberRobotsInQuadrant(
        q2x,
        q2y
    ) * getNumberRobotsInQuadrant(q3x, q3y) * getNumberRobotsInQuadrant(q4x, q4y)
}

private fun List<Robot>.getNumberRobotsInQuadrant(xRange: IntRange, yRange: IntRange): Int {
    return count { it.p.first in xRange && it.p.second in yRange }
}

private fun simulate(robots: List<Robot>, iterations: Int, size: Pair<Int, Int>): List<Robot> {
    var curRobots = robots.toMutableList()

    repeat(iterations) { iter ->
        if (iter == 6475) {
            curRobots.printGrid(size, iter)
        }
        for (i in robots.indices) {
            curRobots[i] = curRobots[i].update(size)
        }

    }

    return curRobots
}

private fun List<Robot>.printGrid(size: Pair<Int, Int>, iteration: Int) {
    println("Iteration: $iteration")
    kotlin.io.println()
    for (i in 0..<size.first) {
        for (j in 0..<size.first) {
            val n = count { it.p.first == i && it.p.second == j }
            print(if (n == 0 || n > 9) "." else n)
        }
        kotlin.io.println()
    }
}

private fun List<Robot>.hasFilledSubGrid(subgridSize: Int, size: Pair<Int, Int>): Boolean {
    val positionSet = this.map { it.p }.toSet()

    fun isGridFilled(x: Int, y: Int): Boolean {
        for (dx in 0 until subgridSize) {
            for (dy in 0 until subgridSize) {
                val position = Pair(x + dx, y + dy)
                if (position !in positionSet) return false
            }
        }
        return true
    }

    for (x in 0..size.first - subgridSize step subgridSize) {
        for (y in 0..size.second - subgridSize step subgridSize) {
            if (isGridFilled(x, y)) return true
        }
    }

    return false
}


private fun Int.wrapToRange(max: Int): Int = ((this % max) + max) % max

private fun Robot.update(size: Pair<Int, Int>): Robot =
    Robot(p = Pair((p.first + v.first).wrapToRange(size.first), (p.second + v.second).wrapToRange(size.second)), v = v)

private fun List<String>.parse(): List<Robot> = map {
    val regex = """p=(-?\d+),(-?\d+)\s+v=(-?\d+),(-?\d+)""".toRegex()
    val matchResult = regex.find(it)
    Robot(
        p = Pair(matchResult!!.groupValues[1].toInt(), matchResult.groupValues[2].toInt()),
        v = Pair(matchResult.groupValues[3].toInt(), matchResult.groupValues[4].toInt())
    )
}