package open.geosolve.canvasdemo.presentation

import moxy.MvpPresenter
import open.geosolve.canvasdemo.model.Mode
import open.geosolve.canvasdemo.model.Node
import open.geosolve.canvasdemo.model.State
import open.geosolve.geosolve.repository.model.Figure

class MainPresenter : MvpPresenter<MainView>() {

    private val figure = Figure()

    private var mode = Mode.ADD_MOVE_FIN
    private var state = State.ON_CANVAS
    private var numOfCall = 0

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.attach(figure)
    }

    fun onTouchDown(touchX: Float, touchY: Float) {
        for (node in figure.nodes) {
            if (node.inRadius(touchX, touchY)) {
                node.isMove = true
                state = State.ON_POINT
                break
            }
        }
    }

    fun onTouchMove(touchX: Float, touchY: Float) {
        for (node in figure.nodes) {
            if (node.isMove) {
                node.moveNode(touchX, touchY)
            }
        }
        numOfCall++
    }

    // TODO Перетаскивание линии с прикреплёнными точками через обработку State.ON_LINE
    fun onTouchUp(touchX: Float, touchY: Float) {
        when (mode) {
            Mode.ADD_MOVE_FIN -> {
                when (state) {
                    State.ON_CANVAS -> {
                        figure.addNode(Node(touchX, touchY))
                        if (figure.nodes.size > 1)
                            figure.addLine(
                                figure.nodes[figure.nodes.size - 2],
                                figure.nodes.last()
                            )
                    }
                    State.ON_POINT -> if (numOfCall < 2)
                        for (node in figure.nodes)
                            if (node.inRadius(touchX, touchY)) {
                                figure.addLine(figure.nodes.last(), node)
                                break
                            }
                }
            }
            Mode.DEL_MOVE -> figure.delNode(touchX, touchY)
            Mode.MARK_FIND -> figure.find = figure.getInRadius(touchX, touchY) ?: figure.find
            Mode.SET_VAlUE -> { /*setValue(touchX, touchY)*/
            }
        }

        numOfCall = 0
        state = State.ON_CANVAS
        figure.stopAllNode()
    }
}