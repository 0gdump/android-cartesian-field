package open.geosolve.canvasdemo.model

import kotlin.properties.Delegates

class Node(
    var x: Float,
    var y: Float
) : Element {

    var isMove = false
    val neighborLines: MutableList<Line> = ArrayList()

    var char by Delegates.notNull<Char>()

    fun moveNode(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    fun inRadius(px: Float, py: Float): Boolean {

        val xInRadius = x - 0.06f < px && px < x + 0.06f
        val yInRadius = y - 0.06f < py && py < y + 0.06f

        return (xInRadius && yInRadius)
    }
    // TODO Переписать систему перетаскивания точек
    fun stopMove(): Boolean {
        return if (isMove) {
            isMove = false
            true
        } else false
    }

}