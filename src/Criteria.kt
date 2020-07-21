enum class Criteria {
    ALL, ANY, NONE;

    fun print(indexes: Array<Int>, everyone: Array<String>) {
        when (this) {
            ALL, ANY -> {
                println("\n${indexes.size} persons found:")
                for (num in indexes) println(everyone[num])
            }
            NONE -> {
                println("\n${everyone.size - indexes.size} persons found:")
                if (indexes.isEmpty()) everyone.forEach { println(it) } else {
                    for (num in everyone.indices) if (!indexes.contains(num)) println(everyone[num])
                }
            }
        }
    }
}