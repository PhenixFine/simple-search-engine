fun main() {
    val words = readLine()!!.split(" ").toTypedArray()
    val search = readLine()!!
    println(if (words.contains(search)) words.indexOf(search) + 1 else "Not Found")
}