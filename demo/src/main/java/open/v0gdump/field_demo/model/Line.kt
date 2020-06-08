package open.v0gdump.field_demo.model

class Line(
    val startPoint: Point,
    val finalPoint: Point
) : Element {

    init {
        check(startPoint != finalPoint) { "Line constructor get the same Node" }

        startPoint.neighborLines.add(this)
        finalPoint.neighborLines.add(this)
    }
}