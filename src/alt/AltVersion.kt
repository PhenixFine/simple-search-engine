package alt

import java.io.File

private lateinit var EVERYONE: Array<String>
private lateinit var EVERYONE_MAP: Map<String, Array<Int>>

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

fun initialize(fileName: String) {
    val muteMap = mutableMapOf<String, MutableList<Int>>()
    val muteLines = mutableListOf<String>()
    var count = 0

    File(fileName).forEachLine {
        muteLines.add(it)
        val split = splitToArray(it)
        for (word in split) {
            if (muteMap.containsKey(word)) {
                muteMap[word]?.add(count)
            } else muteMap[word] = mutableListOf(count)
        }
        count++
    }
    val mapHold = mutableMapOf<String, Array<Int>>()
    for (string in muteMap.keys) mapHold[string] = muteMap[string]!!.toTypedArray()

    EVERYONE_MAP = mapHold.toMap()
    EVERYONE = muteLines.toTypedArray()
}

fun menu() {
    val menu = "=== Menu ===\n1. Find a person\n2. Print all people\n0. Exit"
    var read = getString(menu)
    val num = { read.toIntOrNull() ?: getNum(read, true) }
    var exit = false

    while (!exit) {
        when (num()) {
            1 -> find()
            2 -> printAll()
            0 -> exit = true
            else -> println("\nIncorrect option! Try again.")
        }
        if (!exit) read = getString("\n$menu")
    }
}

fun find() {
    when (val criteria = getString("\nSelect a matching strategy: ALL, ANY, NONE").toLowerCase().trim()) {
        "all", "any", "none" -> findCriteria(criteria)
        else -> {
            println("\nIncorrect option! Try again")
            find()
        }
    }
}

fun findCriteria(criteria: String) {
    var found = true
    val search = splitToArray(getString("\nEnter a name or email to search all suitable people.").toLowerCase())
    val indexes = mutableListOf<Int>()

    for (word in search) {
        if (EVERYONE_MAP.containsKey(word)) {
            if (indexes.isEmpty()) EVERYONE_MAP[word]?.forEach { indexes.add(it) } else {
                val tempList = EVERYONE_MAP[word]?.toList()
                if (criteria == "all") {
                    var done = false
                    while (!done && indexes.isNotEmpty()) {
                        for (num in indexes.indices) {
                            if (!tempList!!.contains(indexes[num])) {
                                indexes.removeAt(num)
                                break
                            }
                        }
                        done = true
                    }
                    if (indexes.isEmpty()) break
                } else for (num in tempList!!) if (!indexes.contains(num)) indexes.add(num)
            }
        } else if (criteria == "all") {
            found = false
            break
        }
    }
    if (indexes.isEmpty() && criteria != "none") found = false
    if (found) printSome(indexes.toTypedArray(), criteria) else println("\nNo matching people found.")
}

fun printSome(indexes: Array<Int>, criteria: String) {
    when (criteria) {
        "all", "any" -> {
            println("\n${indexes.size} persons found:")
            for (num in indexes) println(EVERYONE[num])
        }
        "none" -> {
            println("\n${EVERYONE.size - indexes.size} persons found:")
            if (indexes.isEmpty()) EVERYONE.forEach { println(it) } else {
                for (num in EVERYONE.indices) if (!indexes.contains(num)) println(EVERYONE[num])
            }
        }
    }
}

fun printAll() {
    println("\n=== List of people ===")
    EVERYONE.forEach { println(it) }
}

fun splitToArray(words: String): Array<String> {
    var hold = words.trim()
    while (hold.contains("  ")) hold = hold.replace("  ", " ")
    return hold.toLowerCase().split(" ").toTypedArray()
}

fun getNum(text: String, defaultMessage: Boolean = false): Int {
    val strErrorNum = " was not a number, please try again: "
    var num = text
    var default = defaultMessage

    do {
        num = getString(if (default) num + strErrorNum else num)
        if (!default) default = true
    } while (!isNumber(num))

    return num.toInt()
}

fun getString(text: String): String {
    println(text)
    return readLine()!!
}

fun isNumber(number: String) = number.toIntOrNull() != null