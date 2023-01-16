package com.example.myapplication.ui.main

class Point(val x: Double, val y: Double)

class CoordinateSystem {
    var origin = Point(0.0, 0.0)

    fun moveOrigin(x: Double, y: Double) {
        origin = Point(x, y)
    }

    fun distanceFromOrigin(point: Point): Double {
        val xDiff = point.x - origin.x
        val yDiff = point.y - origin.y
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff)
    }
}
