package open.v0gdump.field_demo.model

class Figure {

    private val _nodes = mutableListOf<Node>()
    val nodes: List<Node>
        get() = _nodes

    private val _lines = mutableListOf<Line>()
    val lines: List<Line>
        get() = _lines

    fun addNode(node: Node) =
        _nodes.add(node)

    fun addLine(startNode: Node, finNode: Node) =
        _lines.add(Line(startNode, finNode))
}