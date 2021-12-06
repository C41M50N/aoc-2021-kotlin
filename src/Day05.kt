
data class LineSegment (val xRange: IntProgression, val yRange: IntProgression)

/*
    functions that given any number of line segments
    returns a map where a single point is the key and
    the number of intersections at that point is the value
*/

private fun getStraightLineSegmentIntersectionMap (segments: List<LineSegment>): Map<Pair<Int,Int>, Int> {
    val pointIntersectionMap = mutableMapOf<Pair<Int,Int>, Int>()

    for (segment in segments) {
        val isVerticalLineSegment   = segment.xRange.first == segment.xRange.last
//        val isHorizontalLineSegment = segment.yRange.first == segment.yRange.last

        if (isVerticalLineSegment) {
            for (yPos in segment.yRange) {
                val xPos = segment.xRange.first
                val currentCountAtPoint = pointIntersectionMap[ Pair(xPos, yPos) ]

                if (currentCountAtPoint != null) {
                    pointIntersectionMap[ Pair(xPos, yPos) ] = currentCountAtPoint + 1
                } else {
                    pointIntersectionMap[ Pair(xPos, yPos) ] = 1
                }
            }
        } else {
            for (xPos in segment.xRange) {
                val yPos = segment.yRange.first
                val currentCountAtPoint = pointIntersectionMap[ Pair(xPos, yPos) ]

                if (currentCountAtPoint != null) {
                    pointIntersectionMap[ Pair(xPos, yPos) ] = currentCountAtPoint + 1
                } else {
                    pointIntersectionMap[ Pair(xPos, yPos) ] = 1
                }
            }
        }
    }

    return pointIntersectionMap
}

private fun getDiagonalLineSegmentIntersectionMap (segments: List<LineSegment>): Map<Pair<Int,Int>, Int> {
    val pointIntersectionMap = mutableMapOf<Pair<Int,Int>, Int>()

    for (segment in segments) {
        val xRange = segment.xRange
        val yRange = segment.yRange

        xRange.zip(yRange).forEach { (xPos, yPos) ->
            val currentCountAtPoint = pointIntersectionMap[ Pair(xPos, yPos) ]

            if (currentCountAtPoint != null) {
                pointIntersectionMap[ Pair(xPos, yPos) ] = currentCountAtPoint + 1
            } else {
                pointIntersectionMap[ Pair(xPos, yPos) ] = 1
            }
        }
    }

    return pointIntersectionMap
}

fun main () {

    fun part1 (lineSegments: List<LineSegment>): Int {
        val straightLineSegments = lineSegments.filter { (it.xRange.first == it.xRange.last) or (it.yRange.first == it.yRange.last) }
        val intersectionMap = getStraightLineSegmentIntersectionMap(straightLineSegments)
        return intersectionMap.count { it.value >= 2 }
    }

    fun part2 (lineSegments: List<LineSegment>): Int {
        val straightLineSegments = lineSegments.filter { (it.xRange.first == it.xRange.last) or (it.yRange.first == it.yRange.last) }
        val straightIntersectionMap = getStraightLineSegmentIntersectionMap(straightLineSegments)
        val diagonalLineSegments = lineSegments.filter { (it.xRange.first != it.xRange.last) and (it.yRange.first != it.yRange.last) }
        val diagonalIntersectionMap = getDiagonalLineSegmentIntersectionMap(diagonalLineSegments)

        val finalIntersectionMap = straightIntersectionMap.interpolate(diagonalIntersectionMap)
        return finalIntersectionMap.count { it.value >= 2 }
    }


    val testInput = readInput("Day05_test")
    val testInputLineSegments = getLineSegmentsFromStrings(testInput)

    val result1 = part1(testInputLineSegments)
    println(result1)
    check(result1 == 5)

    val result2 = part2(testInputLineSegments)
    println(result2)
    check(result2 == 12)


    val input = readInput("Day05")
    val inputLineSegments = getLineSegmentsFromStrings(input)
    println("Part 1 Answer: ${part1(inputLineSegments)}")   // 6283
    println("Part 2 Answer: ${part2(inputLineSegments)}")   // 18864

}

private fun getLineSegmentsFromStrings (input: List<String>): List<LineSegment> {
    val lineSegments = mutableListOf<LineSegment>()
    for (line in input) {
        val (point1String, point2String) = line.split(" -> ")

        val (xStart, yStart) = point1String.split(",").map { it.toInt() }
        val (xEnd, yEnd) = point2String.split(",").map { it.toInt() }

        var xRange: IntProgression = xStart..xEnd
        var yRange: IntProgression = yStart..yEnd
        if (xStart > xEnd) xRange = xStart downTo xEnd
        if (yStart > yEnd) yRange = yStart downTo yEnd

        lineSegments.add(LineSegment(xRange, yRange))
    }
    return lineSegments
}

private fun Map<Pair<Int,Int>, Int>.interpolate (other: Map<Pair<Int,Int>, Int>): Map<Pair<Int,Int>, Int> {

    val uniqueCoordinatesInFirst    = this.keys.minus(other.keys)
    val uniqueCoordinatesInLast     = other.keys.minus(this.keys)

    val firstMap    = this.filter { it.key in uniqueCoordinatesInFirst }
    val lastMap     = other.filter { it.key in uniqueCoordinatesInLast }

    val resultIntersectionMap = mutableMapOf<Pair<Int,Int>, Int>()
    resultIntersectionMap.putAll(firstMap)
    resultIntersectionMap.putAll(lastMap)

    val commonPoints = this.keys.intersect(other.keys)
    for (point in commonPoints) {
        val currentCountAtPoint = resultIntersectionMap.getOrDefault(point, 1)
        resultIntersectionMap[ point ] = currentCountAtPoint + other[ point ]!!
    }

    return resultIntersectionMap
}
