
fun main() {

    fun getBinaryCounts (binaries: List<String>, index: Int): Counts {
        val sample = binaries.sampleAtIndex(index)

        val zerosCount  = sample.count { it == '0' }
        val onesCount   = sample.count { it == '1' }

        return if (zerosCount > onesCount) {
            Counts.MORE_ZEROS
        } else if (onesCount > zerosCount) {
            Counts.MORE_ONES
        } else {
            Counts.EQUAL
        }
    }

    fun part1 (diagnosticReport: List<String>): Int {
        val binaryLength = diagnosticReport[0].length

        var gammaBinary = ""
        var epsilonBinary = ""

        for (i in 0 until binaryLength) {
            val binaryCounts = getBinaryCounts(diagnosticReport, i)
            when (binaryCounts) {
                Counts.MORE_ZEROS -> {
                    gammaBinary += "0"
                    epsilonBinary += "1"
                }
                Counts.MORE_ONES -> {
                    gammaBinary += "1"
                    epsilonBinary += "0"
                }
                else -> {}
            }
        }

        return gammaBinary.toInt(2) * epsilonBinary.toInt(2)
    }

    fun calculateOxygenGeneratorRatingRecursive (binaries: List<String>, currentIndex: Int): Int {
        if (binaries.size == 1) return binaries[0].toInt(2)

        val binaryCounts = getBinaryCounts(binaries, currentIndex)
        val newBinaries = when (binaryCounts) {
            Counts.MORE_ZEROS -> {
                // keep all binaries that equal 0 at the current index
                binaries.filter { it[currentIndex] == '0' }
            }
            Counts.MORE_ONES -> {
                // keep all binaries that equal 1 at the current index
                binaries.filter { it[currentIndex] == '1' }
            }
            Counts.EQUAL -> {
                // keep all binaries that equal 1 at the current index
                binaries.filter { it[currentIndex] == '1' }
            }
        }

        return calculateOxygenGeneratorRatingRecursive(newBinaries, currentIndex+1)
    }

    fun calculateC02ScrubberRatingRecursive (binaries: List<String>, currentIndex: Int): Int {
        if (binaries.size == 1) return binaries[0].toInt(2)

        val binaryCounts = getBinaryCounts(binaries, currentIndex)
        val newBinaries = when (binaryCounts) {
            Counts.MORE_ZEROS -> {
                // keep all binaries that equal 1 at the current index
                binaries.filter { it[currentIndex] == '1' }
            }
            Counts.MORE_ONES -> {
                // keep all binaries that equal 0 at the current index
                binaries.filter { it[currentIndex] == '0' }
            }
            Counts.EQUAL -> {
                // keep all binaries that equal 0 at the current index
                binaries.filter { it[currentIndex] == '0' }
            }
        }

        return calculateC02ScrubberRatingRecursive(newBinaries, currentIndex+1)
    }

    fun part2(commands: List<String>): Int {
        return calculateOxygenGeneratorRatingRecursive(commands, 0) *
                calculateC02ScrubberRatingRecursive(commands, 0)
    }


    val testInput = readInput("Day03_test")
    println(part1(testInput))
    check(part1(testInput) == 198)

    println(part2(testInput))
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println("Part 1 Answer: ${part1(input)}")   // 749376
    println("Part 2 Answer: ${part2(input)}")   // 2372923
}

private fun List<String>.sampleAtIndex(index: Int): String {
    return fold("") { acc, str -> acc + str[index] }
}

private enum class Counts {
    MORE_ZEROS, MORE_ONES, EQUAL
}
