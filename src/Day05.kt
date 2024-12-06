private const val DAY = 5

private data class Rule(
    val dependency: Int,
    val page: Int
)

fun main() {
    fun part1(input: List<String>): Int {
        val rules = input.parseRules()
        val updates = input.parseUpdates()
        return updates.filterByRules(rules).sumOf { it.getMidValue() }
    }

    fun part2(input: List<String>): Int {
        val rules = input.parseRules()
        val updates = input.parseUpdates()
        return updates.filterByRules(rules, filterForValids = false).map { it.sortByRules(rules) }.sumOf { it.getMidValue() }
    }

    val testInput = readInput("Day${DAY.toDayString()}_test")
    check(part1(testInput) == 143)

    val input = readInput("Day${DAY.toDayString()}")
    check(part1(input) == 6267)
    part1(input).println()
    part2(input).println()
}

private fun List<String>.parseRules(): List<Rule> {
    val rulesStrings = this.subList(0, this.indexOfFirst { it.isBlank() })
    return rulesStrings.map { it.split('|') }.map { Rule(dependency = it[0].toInt(), page = it[1].toInt()) }
}

private fun List<String>.parseUpdates(): List<List<Int>> {
    val updatesStrings = this.subList(this.indexOfFirst { it.isBlank() } + 1, this.size)
    return updatesStrings.map { it.split(",").map { it.trim().toInt() } }
}

private fun List<List<Int>>.filterByRules(rules: List<Rule>, filterForValids: Boolean = true): List<List<Int>> {
    val filteredList = mutableListOf<List<Int>>()

    this.forEach { update ->
        if (filterForValids == update.isValid(rules)) {
            filteredList.add(update)
        }
    }

    return filteredList
}

private fun Int.getDependencies(rules: List<Rule>): List<Int> = rules.filter { it.page == this }.map { it.dependency }

private fun List<Int>.getMidValue() = this[this.size / 2]

private fun List<Int>.isValid(rules: List<Rule>): Boolean {
    this.forEachIndexed { index, page ->
        if (this.subList(index + 1, this.size).any { successor -> page.getDependencies(rules).contains(successor) }) {
            return false
        }
    }
    return true
}

private fun Int.hasUnsolvedDependencies(rules: List<Rule>, successors: List<Int>): Boolean =
    this.getDependencies(rules).any { successors.contains(it) }

private fun List<Int>.sortByRules(rules: List<Rule>): List<Int> {
    val pickableItems = this.toMutableList()
    val sortedList = mutableListOf<Int>()

    while (pickableItems.isNotEmpty()) {
        val item = pickableItems.first { !it.hasUnsolvedDependencies(rules, pickableItems) }
        pickableItems.remove(item)
        sortedList.add(item)
    }

    return sortedList
}