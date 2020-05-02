package open.geosolve.canvasdemo.ui.view

interface FigureManipulationsCallback {

    fun isUsed(x: Float, y: Float): Boolean

    fun onMoveStart(x: Float, y: Float)
    fun onMove(x: Float, y: Float)
    fun onMoveFinished(x: Float, y: Float)

    fun onTouch(x: Float, y: Float)
}