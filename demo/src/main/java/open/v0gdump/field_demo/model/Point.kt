package open.v0gdump.field_demo.model

data class Point(
    var x: Float,
    var y: Float,
    var name: Char
) {

    companion object {
        const val RADIUS = 0.15
    }

    fun inRadius(px: Float, py: Float): Boolean {

        val xInRadius = x - RADIUS < px && px < x + RADIUS
        val yInRadius = y - RADIUS < py && py < y + RADIUS

        return (xInRadius && yInRadius)
    }
}