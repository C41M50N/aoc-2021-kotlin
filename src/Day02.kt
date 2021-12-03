
fun main() {

    fun part1(commands: List<String>): Int {
        var horizontalPosition = 0
        var depth = 0

        commands.forEach {
            run {
                val (command, magnitude) = it.split(" ")
                when (command) {
                    "forward" -> horizontalPosition += magnitude.toInt()
                    "up" -> depth -= magnitude.toInt()
                    "down" -> depth += magnitude.toInt()
                }
            }
        }

        return horizontalPosition * depth
    }

    fun part2(commands: List<String>): Int {
        var horizontalPosition = 0
        var depth = 0
        var aim = 0

        commands.forEach {
            run {
                val (command, magnitude) = it.split(" ")
                when (command) {
                    "forward" -> {
                        horizontalPosition += magnitude.toInt()
                        depth += aim * magnitude.toInt()
                    }
                    "up" -> aim -= magnitude.toInt()
                    "down" -> aim += magnitude.toInt()
                }
            }
        }

        return horizontalPosition * depth
    }


    val testInput = readInput("Day02_test")
    println(part1(testInput))
    check(part1(testInput) == 150)

    println(part2(testInput))
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println("Part 1 Answer: ${part1(input)}")   // 1427868
    println("Part 2 Answer: ${part2(input)}")   // 1568138742
}
