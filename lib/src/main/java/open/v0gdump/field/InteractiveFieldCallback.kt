package open.v0gdump.field

interface InteractiveFieldCallback {

    fun isBelongToContent(x: Float, y: Float): Boolean

    fun onMoveStart(x: Float, y: Float)
    fun onMove(x: Float, y: Float)
    fun onMoveFinished(x: Float, y: Float)

    fun onTouchContent(x: Float, y: Float)
    fun onTouchField(x: Float, y: Float)
    fun onAnyTouch(x: Float, y: Float)
}