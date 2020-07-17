import java.io.File

private lateinit var EVERYONE: Array<String>
private val EVERYONE_MAP = mutableMapOf<String, MutableList<Int>>()

fun main(args: Array<String>) {
    var fileFound = true
    try {
        if (args[0] != "--data" && !File(args[1]).isFile) fileFound = false
    } catch (e: Exception) {
        fileFound = false
    }
    if (fileFound) {
        initialize(args[1])
        menu()
    } else {
        print(if (args.isNotEmpty() && args.size == 2) "Loading ${args[1]} failed." else "No filename provided.")
        println(" Please try again.\n")
    }
}

fun initialize(fileName: String) {
    val muteLines = mutableListOf<String>()
    var count = 0

    File(fileName).forEachLine {
        muteLines.add(it)
        var hold = it.trim()
        while (hold.contains("  ")) hold = hold.replace("  ", " ")
        val split = hold.toLowerCase().split(" ").toTypedArray()
        for (word in split) {
            if (EVERYONE_MAP.containsKey(word)) {
                EVERYONE_MAP[word]?.add(count)
            } else EVERYONE_MAP[word] = mutableListOf(count)
        }
        count++
    }
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
    val search = getString("\nEnter a name or email to search all suitable people.").toLowerCase().trim()
    if (EVERYONE_MAP.containsKey(search)) {
        println("${EVERYONE_MAP[search]?.size} persons found:")
        EVERYONE_MAP[search]?.forEach { println(EVERYONE[it]) }
    } else println("No matching people found.")
}

fun printAll() {
    println("\n=== List of people ===")
    EVERYONE.forEach { println(it) }
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