private lateinit var EVERYONE: Array<String>

fun main() {
    initialize()
    menu()
}

fun initialize() {
    val people = getNum("Enter the number of people:")
    println("Enter all people:")
    EVERYONE = Array(people) { readLine()!! }
}

fun menu() {
    val menu = "\n=== Menu ===\n1. Find a person\n2. Print all people\n0. Exit"
    var num: Int
    var exit = false
    do {
        val read = getString(menu)
        num = read.toIntOrNull() ?: getNum(read, true)
        when (num) {
            1 -> find()
            2 -> printAll()
            0 -> exit = true
            else -> println("\nIncorrect option! Try again.")
        }
    } while (!exit)
}

fun find() {
    val search = getString("\nEnter a name or email to search all suitable people.").trim()
    var found = false
    EVERYONE.forEach {
        if (it.toLowerCase().contains(search.toLowerCase())) {
            if (!found) found = true
            println(it)
        }
    }
    if (!found) println("No matching people found.")
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