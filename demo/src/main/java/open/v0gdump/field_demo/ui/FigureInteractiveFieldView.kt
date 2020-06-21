package open.v0gdump.field_demo.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import open.v0gdump.field.InteractiveFieldView
import open.v0gdump.field.TextAnchor
import open.v0gdump.field_demo.R
import open.v0gdump.field_demo.model.Point
import open.v0gdump.field_demo.model.Polygon

class FigureInteractiveFieldView : InteractiveFieldView {

    //region Drawing data

    private val pointRadius
        get() = dc(Point.RADIUS)

    private val lineThickness
        get() = dp(2 * scale)

    //endregion

    //region Paints

    private val paintNode = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.color_node)
        strokeWidth = pointRadius
    }

    private val paintLine = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.color_line)
        strokeWidth = lineThickness
    }

    //endregion

    //region Polygon

    private var attachedPolygon: Polygon? = null

    fun attach(polygon: Polygon?) {
        attachedPolygon = polygon
    }

    //endregion

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas) {
        super.drawGrid(canvas)

        if (attachedPolygon != null) {
            drawLines(canvas)
            drawNodes(canvas)
        }

        super.drawNotations(canvas)
    }

    private fun drawLines(canvas: Canvas) {

        if (attachedPolygon == null) return

        for (line in attachedPolygon!!.lines) {
            drawLine(
                canvas,
                line.first.x, line.first.y,
                line.second.x, line.second.y,
                paintLine
            )
        }
    }

    private fun drawNodes(canvas: Canvas) {

        if (attachedPolygon == null) return

        attachedPolygon!!.points.forEach { node ->
            drawNode(canvas, node)
            drawNodesName(canvas)
        }
    }

    private fun drawNodesName(canvas: Canvas) {

        if (attachedPolygon == null) return

        for (node in attachedPolygon!!.points) {
            drawText(
                node.name.toString(),
                canvas,
                node.x,
                node.y,
                paintNotations,
                TextAnchor.TopLeft,
                pointRadius
            )
        }
    }

    private fun drawNode(canvas: Canvas, point: Point) {
        drawCircle(canvas, point.x, point.y, pointRadius, paintNode)
    }
}