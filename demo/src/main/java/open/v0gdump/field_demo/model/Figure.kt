package open.v0gdump.field_demo.model

class Figure {

    private val _points = mutableListOf<Point>()
    val points: List<Point>
        get() = _points

    private val _lines = mutableListOf<Line>()
    val lines: List<Line>
        get() = _lines

    fun addNode(point: Point) =
        _points.add(point)

    fun addLine(first: Point, second: Point) =
        _lines.add(Line(first, second))
}