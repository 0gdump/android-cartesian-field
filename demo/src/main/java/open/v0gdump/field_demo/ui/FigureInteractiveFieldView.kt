package open.v0gdump.field_demo.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import open.v0gdump.field.InteractiveFieldView
import open.v0gdump.field.TextAnchor
import open.v0gdump.field_demo.R
import open.v0gdump.field_demo.model.Figure
import open.v0gdump.field_demo.model.Point

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

    private val paintAngle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.color_angle)
        textSize = lineThickness
    }

    //endregion

    //region Figure

    private var attachedFigure: Figure? = null

    fun attach(figure: Figure?) {
        attachedFigure = figure
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

        if (attachedFigure != null) {
            drawLines(canvas)
            drawNodes(canvas)
        }

        super.drawNotations(canvas)
    }

    private fun drawLines(canvas: Canvas) {

        if (attachedFigure == null) return

        for (line in attachedFigure!!.lines) {
            drawLine(
                canvas,
                line.startPoint.x, line.startPoint.y,
                line.finalPoint.x, line.finalPoint.y,
                paintLine
            )
        }
    }

    private fun drawNodes(canvas: Canvas) {

        if (attachedFigure == null) return

        attachedFigure!!.points.forEach { node ->
            drawNode(canvas, node)
            drawNodesName(canvas)
        }
    }

    private fun drawNodesName(canvas: Canvas) {

        if (attachedFigure == null) return

        for (node in attachedFigure!!.points) {
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