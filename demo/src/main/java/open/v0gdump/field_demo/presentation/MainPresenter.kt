package open.v0gdump.field_demo.presentation

import moxy.MvpPresenter
import open.v0gdump.field_demo.model.Point
import open.v0gdump.field_demo.model.Polygon

class MainPresenter : MvpPresenter<MainView>() {

    private val polygon = Polygon()
    private var movedPoint: Point? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.attach(polygon)
    }

    fun isUsedByContent(x: Float, y: Float): Boolean {
        polygon.points.forEach { node ->
            if (node.inRadius(x, y)) {
                return true
            }
        }

        return false
    }

    fun onMoveStart(x: Float, y: Float) {
        polygon.points.forEach { node ->
            if (node.inRadius(x, y)) {
                movedPoint = node
                return
            }
        }

        throw RuntimeException("Point not found, but isUsedByContent work out")
    }

    fun onMove(x: Float, y: Float) {
        movedPoint?.x = x
        movedPoint?.y = y
    }

    fun onMoveFinished(x: Float, y: Float) {
        movedPoint = null
    }

    fun onTouchContent(x: Float, y: Float) {
        if (!polygon.isClosed) {
            polygon.close()
        }
    }

    fun onTouchField(x: Float, y: Float) {
        if (!polygon.isClosed) {
            polygon.appendPoint(x, y)
        }
    }

    fun onAnyTouch(x: Float, y: Float) {
        // Unused
    }
}