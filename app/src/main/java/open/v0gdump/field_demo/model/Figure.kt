package open.v0gdump.geosolve.repository.model

import open.v0gdump.field_demo.model.Element
import open.v0gdump.field_demo.model.Line
import open.v0gdump.field_demo.model.Node

class Figure {

    var find: Element? = null

    private val _nodes = mutableListOf<Node>()
    val nodes: List<Node>
        get() = _nodes

    private val _lines = mutableListOf<Line>()
    val lines: List<Line>
        get() = _lines

    fun addNode(node: Node) = _nodes.add(node)

    fun addLine(startNode: Node, finNode: Node) {
        _lines.add(Line(startNode, finNode))
    }
}