package open.v0gdump.field

interface InteractiveFieldCallback {

    fun isUsedByContent(x: Float, y: Float): Boolean

    fun onMoveStart(x: Float, y: Float)
    fun onMove(x: Float, y: Float)
    fun onMoveFinished(x: Float, y: Float)

    fun onTouch(x: Float, y: Float)
}