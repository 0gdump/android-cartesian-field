package open.geosolve.canvasdemo.model

class Node(
    var x: Float,
    var y: Float
) : Element {

    var isMove = false
    val neighborLines: MutableList<Line> = ArrayList()

    var outerAngle: Float? = null
        private set

    var innerAngle: Float? = null
        private set

    // TODO Защита от дурака
    fun setInnerAngle(value: Float?) {
        innerAngle = value
        outerAngle = if (value != null) 360f - value else null
    }

    // TODO Защита от дурака
    fun setOuterAngle(value: Float?) {
        outerAngle = value
        innerAngle = if (value != null) 360f - value else null
    }

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