package open.geosolve.geosolve.repository.model

import open.geosolve.canvasdemo.model.Element
import open.geosolve.canvasdemo.model.Line
import open.geosolve.canvasdemo.model.Node

class Figure {

    var find: Element? = null

    private val _nodes = mutableListOf<Node>()
    val nodes: List<Node>
        get() = _nodes

    private val _lines = mutableListOf<Line>()
    val lines: List<Line>
        get() = _lines

    fun stopAllNode(): Boolean {
        var answer = false
        for (node in _nodes) {
            answer = answer or node.stopMove()
        }
        return answer
    }

    fun addNode(node: Node) = _nodes.add(node)

    fun addLine(startNode: Node, finNode: Node) {
        _lines.add(Line(startNode, finNode))
    }

    fun delNode(touchX: Float, touchY: Float) {
        for (node in _nodes)
            if (node.inRadius(touchX, touchY)) {
                val index: Int = _nodes.indexOf(node)
                for (line in node.neighborLines) {
                    _lines.remove(line)
                    if (find == line)
                        find = null
                }

                _nodes.removeAt(index)
                break
            }
    }

    fun clearFigure() {
        _nodes.clear()
        _lines.clear()
        find = null
    }

    fun getInRadius(x: Float, y: Float): Element? {
        var returnElem: Element? = null

        for (line in _lines) {
            if (line.inRadius(x, y)) {
                returnElem = line
                break
            }
        }
        for (node in _nodes) {
            if (node.inRadius(x, y)) {
                returnElem = node
                break
            }
        }

        return returnElem
    }
}