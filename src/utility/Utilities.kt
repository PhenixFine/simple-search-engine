package utility

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