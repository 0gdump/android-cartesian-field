package open.v0gdump.field_demo.presentation

import moxy.MvpPresenter
import open.v0gdump.field_demo.model.Figure
import open.v0gdump.field_demo.model.Point

class MainPresenter : MvpPresenter<MainView>() {

    private val figure = Figure()
    private var figureClosed = false
    private var movedPoint: Point? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.attach(figure)
    }

    fun isUsedByContent(x: Float, y: Float): Boolean {
        figure.points.forEach { node ->
            if (node.inRadius(x, y)) return true
        }

        return false
    }

    fun onMoveStart(x: Float, y: Float) {
        figure.points.forEach { node ->
            if (node.inRadius(x, y)) {
                movedPoint = node
                return
            }
        }

        throw RuntimeException("Node not found, but isUsed work's")
    }

    fun onMove(x: Float, y: Float) {
        movedPoint?.x = x
        movedPoint?.y = y
    }

    fun onMoveFinished(x: Float, y: Float) {
        movedPoint = null
    }

    fun onTouchContent(x: Float, y: Float) {
        figureClosed = true

        figure.addLine(
            figure.points.first(),
            figure.points.last()
        )
    }

    fun onTouchField(x: Float, y: Float) {
        if (!figureClosed) {

            figure.addNode(Point(x, y))

            if (figure.points.size > 1) {
                figure.addLine(
                    figure.points[figure.points.size - 2],
                    figure.points.last()
                )
            }
        }
    }

    fun onAnyTouch(x: Float, y: Float) {
        val charRange = ('A'..'Z').toList()
        for (i in figure.points.indices) {
            figure.points[i].name = charRange[i]
        }
    }
}