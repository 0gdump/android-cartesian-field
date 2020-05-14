package open.v0gdump.field_demo.model

import kotlin.properties.Delegates

class Node(
    var x: Float,
    var y: Float
) : Element {

    companion object {
        const val RADIUS = 0.15
    }

    var name by Delegates.notNull<Char>()

    val neighborLines: MutableList<Line> = ArrayList()
    
    fun inRadius(px: Float, py: Float): Boolean {

        val xInRadius = x - RADIUS < px && px < x + RADIUS
        val yInRadius = y - RADIUS < py && py < y + RADIUS

        return (xInRadius && yInRadius)
    }
}