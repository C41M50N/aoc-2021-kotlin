
fun main() {
    fun part1(depths: List<Int>): Int {
        var numDepthsIncreased = 0

        val depthsIterator = depths.iterator()
        var previous = depthsIterator.next()
        while (depthsIterator.hasNext()) {
            val current = depthsIterator.next()

            if (current > previous) numDepthsIncreased++

            previous = current
        }

        return numDepthsIncreased
    }

    fun part1UsingWindowed(depths: List<Int>): Int {
        return depths.windowed(2).count { (previous, current) -> current > previous }
    }

    fun part2(depths: List<Int>): Int {
        var numDepthsIncreased = 0

        var startIndex = 0
        var endIndex = 2

        var previous = depths.getSumOfRange(startIndex..endIndex)
        while (endIndex + 1 < depths.size) {
            val current = depths.getSumOfRange(++startIndex..++endIndex)

            if (current > previous) numDepthsIncreased++

            previous = current
        }

        return numDepthsIncreased
    }

    fun part2UsingWindowed(depths: List<Int>): Int {
        return depths.windowed(4).count {
            (a,b,c,d) ->
                run {
                    val previousSum = a + b + c
                    val currentSum = b + c + d
                    currentSum > previousSum
                }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInputInt("Day01_test")
    println(part1(testInput) == 7)
    check(part1(testInput) == 7)

    val input = readInputInt("Day01")
    println(part1(input))
    println(part1UsingWindowed(input))   // 1298
    println(part2(input))
    println(part2UsingWindowed(input))   // 1248
}

private fun List<Int>.getSumOfRange(range: IntRange): Int {
    return this.slice(range).sum()
}
