package open.v0gdump.field_demo.model

class Line(
    val first: Point,
    val second: Point,
    var name: String = "[${first.name}${second.name}]"
)