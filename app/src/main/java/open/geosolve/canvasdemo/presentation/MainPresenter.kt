package open.geosolve.canvasdemo.presentation

import moxy.MvpPresenter
import open.geosolve.canvasdemo.model.Mode
import open.geosolve.canvasdemo.model.Node
import open.geosolve.canvasdemo.model.State
import open.geosolve.geosolve.repository.model.Figure

class MainPresenter : MvpPresenter<MainView>() {

    private val figure = Figure()
    private var figureClosed = false
    private var movedNode: Node? = null

    private var mode = Mode.ADD_MOVE_FIN
    private var state = State.ON_CANVAS
    private var numOfCall = 0

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.attach(figure)
    }

    fun isUsed(x: Float, y: Float): Boolean {
        figure.nodes.forEach { node ->
            if (node.inRadius(x, y)) return true
        }

        return false
    }


    fun onMoveStart(x: Float, y: Float) {
        figure.nodes.forEach { node ->
            if (!node.inRadius(x, y)) return
            movedNode = node
        }

        throw RuntimeException("WTF. Node not found")
    }

    fun onMove(x: Float, y: Float) {
        movedNode?.moveNode(x, y)
    }

    fun onMoveFinished(x: Float, y: Float) {
        movedNode = null
    }

    fun onTouch(x: Float, y: Float) {

        var isCanvasTouch = true

        figure.nodes.forEach { node ->
            if (node.inRadius(x, y)) {
                isCanvasTouch = false
                return@forEach
            }
        }

        if (isCanvasTouch && !figureClosed) {
            figure.addNode(Node(x, y))

            if (figure.nodes.size > 1) {
                figure.addLine(
                    figure.nodes[figure.nodes.size - 2],
                    figure.nodes.last()
                )
            }
        } else {
            figureClosed = true

            figure.addLine(
                figure.nodes.first(),
                figure.nodes.last()
            )
        }
    }
}