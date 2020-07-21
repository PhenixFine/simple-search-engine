import utility.*
import java.io.File

private lateinit var EVERYONE: Array<String>
private lateinit var CRITERIA: Criteria
private val EVERYONE_MAP = mutableMapOf<String, MutableList<Int>>()

fun main(args: Array<String>) {
    val filename: String
    var fileFound = true

    try {
        if (args[0] != "--data" || !File(args[1]).isFile) fileFound = false
    } catch (e: Exception) {
        fileFound = false
    }

    filename = if (!fileFound) {
        print(if (args.isNotEmpty() && args.size == 2) "Loading ${args[1]} failed." else "No filename provided.")
        println(" Loading default file names.txt for searching.\n")
        "src/names.txt"
    } else args[1]

    initialize(filename)
    menu()
    println("\nBye!")
}

private fun initialize(fileName: String) {
    val muteLines = mutableListOf<String>()
    var count = 0

    File(fileName).forEachLine {
        muteLines.add(it)
        val split = splitToArray(it)
        for (word in split) {
            if (EVERYONE_MAP.containsKey(word)) {
                EVERYONE_MAP[word]?.add(count)
            } else EVERYONE_MAP[word] = mutableListOf(count)
        }
        count++
    }
    EVERYONE = muteLines.toTypedArray()
}

private fun menu() {
    val menu = "=== Menu ===\n1. Find a person\n2. Print all people\n0. Exit"
    var read = getString(menu)
    val num = { read.toIntOrNull() ?: getNum(read, true) }
    var exit = false

    while (!exit) {
        when (num()) {
            1 -> find()
            2 -> printAll()
            0 -> exit = true
            else -> incorrectOption()
        }
        if (!exit) read = getString("\n$menu")
    }
}

private fun find() {
    CRITERIA = when (getString("\nSelect a matching strategy: ALL, ANY, NONE").toLowerCase().trim()) {
        "all" -> Criteria.ALL
        "any" -> Criteria.ANY
        "none" -> Criteria.NONE
        else -> {
            incorrectOption()
            find()
            return
        }
    }
    findCriteria()
}

private fun findCriteria() {
    var found = true
    val search = splitToArray(getString("\nEnter a name or email to search all suitable people.").toLowerCase())
    var indexes = listOf<Int>()

    for (word in search) {
        if (EVERYONE_MAP.containsKey(word)) {
            if (indexes.isEmpty()) indexes = EVERYONE_MAP[word]!! else {
                if (CRITERIA == Criteria.ALL) {
                    indexes = indexes.intersect(EVERYONE_MAP[word]!!).toList()
                    if (indexes.isEmpty()) break
                } else indexes = indexes.union(EVERYONE_MAP[word]!!).toList()
            }
        } else if (CRITERIA == Criteria.ALL) {
            found = false
            break
        }
    }
    if (found && indexes.isEmpty() && CRITERIA != Criteria.NONE) found = false
    if (found) CRITERIA.print(indexes.toTypedArray(), EVERYONE) else println("\nNo matching people found.")
}

private fun printAll() {
    println("\n=== List of people ===")
    EVERYONE.forEach { println(it) }
}

private fun incorrectOption() = println("\nIncorrect option! Please try again.")