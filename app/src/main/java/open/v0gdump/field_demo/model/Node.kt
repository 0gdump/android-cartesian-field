package open.v0gdump.field_demo.model

import kotlin.properties.Delegates

class Node(
    var x: Float,
    var y: Float
) : Element {

    val neighborLines: MutableList<Line> = ArrayList()

    var char by Delegates.notNull<Char>()

    fun inRadius(px: Float, py: Float): Boolean {

        val xInRadius = x - 0.06f < px && px < x + 0.06f
        val yInRadius = y - 0.06f < py && py < y + 0.06f

        return (xInRadius && yInRadius)
    }
}