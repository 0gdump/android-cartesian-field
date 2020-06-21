package open.v0gdump.field_demo.model

class Polygon {

    private val letters = ('a'..'z').iterator()

    private val _points = mutableListOf<Point>()
    val points: List<Point>
        get() = _points

    private val _lines = mutableListOf<Line>()
    val lines: List<Line>
        get() = _lines

    val isClosed: Boolean
        get() {
            return if (_lines.size <= 1 || _points.size <= 1) {
                false
            } else {
                val lastLine = _lines.last()
                val firstPoint = _points.first()
                val secondPoint = _points.last()

                (lastLine.first == firstPoint && lastLine.second == secondPoint)
            }
        }

    fun appendPoint(x: Float, y: Float) {

        check(!isClosed) { "Polygon already closed" }
        check(_points.size < 26) { "Can't have more than 26 points" }

        _points.add(Point(x, y, letters.nextChar()))

        if (_points.size > 1) {
            _lines += Line(
                _points[_points.lastIndex - 1],
                _points[_points.lastIndex - 0]
            )
        }
    }

    fun close() {
        check(!isClosed) { "Polygon already closed" }

        _lines += Line(
            _points.first(),
            _points.last()
        )
    }
}