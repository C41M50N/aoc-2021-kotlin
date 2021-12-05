
class BingoBoardNumber (val number: Int, val xPosition: Int, val yPosition: Int) {
    var isMarked: Boolean = false
    fun mark () { this.isMarked = true }
}

class BingoBoard (private val numbers: List<BingoBoardNumber>) {
    companion object{
        fun fromIntList (list: List<Int>): BingoBoard {
            val bingoBoardNumbers = mutableListOf<BingoBoardNumber>()

            var xPos = 0
            var yPos = 0
            for (number in list) {
                if (xPos == 5) {
                    xPos = 0
                    yPos += 1
                }
                bingoBoardNumbers.add(BingoBoardNumber(number, xPos++, yPos))
            }

            return BingoBoard(bingoBoardNumbers)
        }
    }

    fun pick (number: Int) {
        val targetIndex = numbers.indexOf( this.numbers.find { it.number == number } )
        if (targetIndex != -1) this.numbers[targetIndex].mark()
    }

    fun hasBingo (): Boolean {
        for (i in 0..4) {
            val isHorizontalBingo   = numbers.filter { it.xPosition == i }.all { it.isMarked }
            val isVerticalBingo     = numbers.filter { it.yPosition == i }.all { it.isMarked }

            if (isHorizontalBingo or isVerticalBingo) return true
        }
        return false
    }

    fun unmarkedNumbersSum (): Int {
        return numbers.filter { !it.isMarked }.sumOf { it.number }
    }
}

class BingoSpeedrun (private val numberQueue: List<Int>,
                     private val boards: List<BingoBoard>) {

    fun executeFindFirstWin (): Pair<Int, Int> {
        for (number in numberQueue) {
            boards.forEach { it.pick(number) }

            val completeBingoBoards = boards.filter { it.hasBingo() }
            if (completeBingoBoards.size == 1) {
                return Pair(number, completeBingoBoards[0].unmarkedNumbersSum())
            }
        }
        return Pair(0,0)
    }

    fun executeFindLastWin (): Pair<Int, Int> {
        for (number in numberQueue) {
            val completeBingoBoardsBefore = boards.filter { it.hasBingo() }
            boards.forEach { it.pick(number) }
            val completeBingoBoardsAfter = boards.filter { it.hasBingo() }

            if (completeBingoBoardsAfter.size == boards.size) {
                val lastBingoBoard = completeBingoBoardsAfter.minus(completeBingoBoardsBefore.toSet())[0]
                return Pair(number, lastBingoBoard.unmarkedNumbersSum())
            }
        }
        return Pair(0,0)
    }
}

fun main() {

    fun part1 (numberQueue: List<Int>, bingoBoards: List<BingoBoard>): Int {
        val resultPair = BingoSpeedrun(numberQueue, bingoBoards).executeFindFirstWin()
        println("Bingo number: ${resultPair.first} | Unmarked sum: ${resultPair.second}")
        return resultPair.first * resultPair.second
    }

    fun part2 (numberQueue: List<Int>, bingoBoards: List<BingoBoard>): Int {
        val resultPair = BingoSpeedrun(numberQueue, bingoBoards).executeFindLastWin()
        println("Bingo number: ${resultPair.first} | Unmarked sum: ${resultPair.second}")
        return resultPair.first * resultPair.second
    }


    val testInput = readInput("Day04_test")
    val testInputPair = getNumberQueueAndBoardFromInput(testInput)

    val result1 = part1(testInputPair.first, testInputPair.second)
    println(result1)
    check(result1 == 4512)

    val result2 = part2(testInputPair.first, testInputPair.second)
    println(result2)
    check(result2 == 1924)


    val input = readInput("Day04")
    val inputPair = getNumberQueueAndBoardFromInput(input)
    println("Part 1 Answer: ${part1(inputPair.first, inputPair.second)}")   // 49860
    println("Part 2 Answer: ${part2(inputPair.first, inputPair.second)}")   // 24628
}

private fun getNumberQueueAndBoardFromInput (input: List<String>): Pair<List<Int>, List<BingoBoard>> {
    val numberQueue = input[0].split(",").map { it.toInt() }
    val bingoBoards = mutableListOf<BingoBoard>()

    val currentBoard = mutableListOf<Int>()
    for (i in 2 until input.size) {
        if (input[i].isNotEmpty()) {
            currentBoard.addAll(input[i].split(" ").filter { it.isNotEmpty() }.map { it.toInt() })
        } else {
            bingoBoards.add(BingoBoard.fromIntList(currentBoard))
            currentBoard.clear()
        }
    }
    bingoBoards.add(BingoBoard.fromIntList(currentBoard))

    return Pair(numberQueue, bingoBoards)
}
