package open.v0gdump.field_demo.model

import kotlin.properties.Delegates

class Node(
    var x: Float,
    var y: Float
) : Element {

    companion object {
        const val RADIUS = 0.15
    }

    val neighborLines: MutableList<Line> = ArrayList()

    var char by Delegates.notNull<Char>()

    fun inRadius(px: Float, py: Float): Boolean {

        val safetyRadius = RADIUS + 0.05

        val xInRadius = x - safetyRadius < px && px < x + safetyRadius
        val yInRadius = y - safetyRadius < py && py < y + safetyRadius

        return (xInRadius && yInRadius)
    }
}