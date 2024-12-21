package Day03

import println
import readInput
import toDayString

private const val DAY = 3

fun main() {
    fun part1(input: List<String>): Int {
        return input.joinToString().parse().filterIsInstance<Operations.MUL>().sumOf { it.a * it.b }
    }

    fun part2(input: List<String>): Int {
        return input.joinToString().parse().filterExcludedOperations().sumOf { it.a * it.b }
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 161)

    val input = readInput("Day${DAY.toDayString()}")
    check(part1(input) == 187194524)
    part1(input).println()
    part2(input).println()
}

private fun String.parse(): List<Operations> {
    val regex = Regex("(mul\\((\\d{1,3}),(\\d{1,3})\\))|(do\\(\\))|(don't\\(\\))")
    val matches = regex.findAll(this)

    val pairs = mutableListOf<Operations>()
    matches.forEach { match ->
        when (val fullMatch = match.value) {
            "do()" -> pairs.add(Operations.DO)
            "don't()" -> pairs.add(Operations.DONT)
            else -> {
                val numbers = fullMatch.substring(4..fullMatch.length - 2).split(',')
                pairs.add(Operations.MUL(a = numbers[0].toInt(), b = numbers[1].toInt()))
            }
        }
    }
    return pairs
}

private fun List<Operations>.filterExcludedOperations(): List<Operations.MUL> {
    val filteredList = mutableListOf<Operations.MUL>()
    var exclude = false
    this.forEach { element ->
        when (element) {
            Operations.DO -> exclude = false
            Operations.DONT -> exclude = true
            is Operations.MUL -> {
                if (!exclude) {
                    filteredList.add(element)
                }
            }
        }
    }
    return filteredList.toList()
}

private sealed class Operations {
    data object DO : Operations()
    data object DONT : Operations()
    data class MUL(val a: Int, val b: Int) : Operations()
}