package Day09

import println
import readInput
import toDayString

private const val DAY = 9

private sealed class Address(open val startPos: Int) {
    data class Empty(override val startPos: Int = 0, val length: Int = 0) : Address(startPos)
    data class Filled(val index: Int, override val startPos: Int = 0, val length: Int = 0) : Address(startPos)

    override fun toString(): String {
        return when (this) {
            is Empty -> "."
            is Filled -> this.index.toString()
        }
    }

    fun getValue() = when (this) {
        is Filled -> this.index
        is Empty -> 0
    }
}

fun main() {
    fun part1(input: List<String>): Long {
        return input[0].expand().moveFiles().calcChecksum()
    }

    fun part2(input: List<String>): Long {
        return input[0].format().moveFiles2().calcChecksum2()
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 1928L)

    val input = readInput("Day${DAY.toDayString()}")
    check(part1(input) == 6154342787400)
    check(part2(testInput) == 2858L)
    part1(input).println()
    part2(input).println()
}

private fun MutableList<Address>.calcChecksum(): Long =
    mapIndexed { pos, it -> it.getValue().toLong() * pos.toLong() }.sum()

private fun List<Address>.calcChecksum2(): Long =
    filterIsInstance<Address.Filled>()
        .sumOf { filled ->
            (0 until filled.length)
                .sumOf { offset ->
                    (filled.startPos + offset).toLong() * filled.index.toLong()
                }
        }


private fun MutableList<Address>.moveFiles(): MutableList<Address> {
    while (true) {
        val a = indexOfLast { it is Address.Filled }
        val b = indexOfFirst { it is Address.Empty }
        if (a + 1 == b) {
            return this
        }
        swap(a, b)
    }
}

private fun MutableList<Address>.moveFiles2(): MutableList<Address> {
    filterIsInstance<Address.Filled>().reversed().forEach { filled ->
        filterIsInstance<Address.Empty>().firstOrNull { it.length >= filled.length }?.let { empty ->
            val newStartPos = empty.startPos
            if (newStartPos < filled.startPos) {
                val updatedEmptyLength = empty.length - filled.length
                val updatedEmptyStart = empty.startPos + filled.length

                this.remove(filled)
                this.remove(empty)

                this.add(Address.Filled(filled.index, newStartPos, filled.length))

                if (updatedEmptyLength > 0) {
                    this.add(Address.Empty(updatedEmptyStart, updatedEmptyLength))
                }

                this.sortBy { it.startPos }
            }
        }
    }

    return this
}

private fun String.format(): MutableList<Address> {
    val formatted = mutableListOf<Address>()
    var curPos = 0

    forEachIndexed { i, length ->
        when {
            i % 2 == 0 -> {
                formatted.add(Address.Filled(index = i / 2, startPos = curPos, length = length.digitToInt()))
            }

            else -> {
                formatted.add(Address.Empty(startPos = curPos, length = length.digitToInt()))
            }
        }
        curPos += length.digitToInt()
    }

    return formatted
}


private fun MutableList<Address>.swap(a: Int, b: Int) {
    val tmp = this[a]
    this[a] = this[b]
    this[b] = tmp
}

private fun String.expand(): MutableList<Address> {
    val expanded = mutableListOf<Address>()

    forEachIndexed { i, value ->
        when {
            i % 2 == 0 -> {
                for (t in 1..value.digitToInt()) {
                    expanded.add(Address.Filled(index = i / 2, startPos = 0, length = 0))
                }
            }

            else -> {
                for (t in 1..value.digitToInt()) {
                    expanded.add(Address.Empty())
                }
            }
        }
    }
    return expanded
}