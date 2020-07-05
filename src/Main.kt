fun main() {
    val people = getNum("Enter the number of people:")
    println("Enter all people:")
    val everyone: Array<String> = Array(people) { readLine()!! }
    println()
    val searchNum = getNum("Enter the number of search queries:")

    repeat(searchNum) {
        println()
        val search = getString("Enter data to search people:").trim()
        println()
        var found = false
        everyone.forEach {
            if (it.toLowerCase().contains(search.toLowerCase())) {
                if (!found) {
                    println("Found people:")
                    found = true
                }
                println(it)
            }
        }
        if (!found) println("No matching people found.")
    }
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