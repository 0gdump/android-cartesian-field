package open.v0gdump.field_demo.model

class Line(
    val startNode: Node,
    val finalNode: Node
) : Element {

    init {
        check(startNode != finalNode) { "Line constructor get the same Node" }

        startNode.neighborLines.add(this)
        finalNode.neighborLines.add(this)
    }
}