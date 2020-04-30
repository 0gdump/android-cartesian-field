package open.geosolve.canvasdemo.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import open.geosolve.canvasdemo.R
import open.geosolve.canvasdemo.model.Node
import open.geosolve.geosolve.repository.model.Figure
import kotlin.math.roundToInt

open class SimpleCanvasView : View {

    private var scale = 1.0f

    private val gridStep
        get() = 120 * scale

    private val pointRadius
        get() = 20 * scale

    private val lineThickness
        get() = 1 * scale

    private val paintXY = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        strokeWidth = 5 * scale
    }

    private val paintGrid = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.LTGRAY
    }

    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 42 * scale
    }

    private val mPaintNode = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.color_node)
        strokeWidth = pointRadius
    }

    private val mPaintLine = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.color_line)
        strokeWidth = lineThickness
    }

    private val mPaintAngle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.color_angle)
        textSize = lineThickness
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var attachedFigure: Figure? = null

    fun attach(figure: Figure?) {
        attachedFigure = figure
    }

    fun updateScale(scale: Float) {
        this.scale = scale
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawGrid(canvas)
        drawFigure(canvas)
        drawText(canvas)
    }

    private fun drawFigure(canvas: Canvas) {
        if (attachedFigure == null) {
            canvas.drawColor(Color.WHITE)
            return
        }

        drawLines(canvas)
        drawNodes(canvas)
    }

    private fun drawLines(canvas: Canvas) {
        if (attachedFigure == null) return

        for (line in attachedFigure!!.lines) {
            canvas.drawLine(
                line.startNode.x, line.startNode.y,
                line.finalNode.x, line.finalNode.y,
                mPaintLine
            )
        }
    }

    private fun drawNodes(canvas: Canvas) {
        if (attachedFigure == null) return

        attachedFigure!!.nodes.forEach { node ->
            drawNode(canvas, node)
            drawAngleDecoration(canvas, node)
        }
    }

    private fun drawNode(canvas: Canvas, node: Node) {
        canvas.drawCircle(node.x, node.y, pointRadius, mPaintNode)
    }

    // TODO Рисовать дугу угла
    // TODO Рисовать внешний угол
    // TODO Подстраивать положение текста, чтобы не наезжал на линии
    private fun drawAngleDecoration(canvas: Canvas, node: Node) {
        if (node.innerAngle == null) return

        canvas.drawText(
            node.innerAngle.toString(),
            node.x + 50,
            node.y + 50,
            mPaintAngle
        )
    }

    private fun drawGrid(canvas: Canvas) {
        drawX(canvas)
        drawY(canvas)
    }

    private fun drawX(canvas: Canvas) {

        val width = canvas.width.toFloat()
        val height = canvas.height.toFloat()

        val cx = width / 2f
        var cxOffset = gridStep

        canvas.drawLine(
            cx,
            0f,
            cx,
            height,
            paintXY
        )

        while (true) {

            // Left
            canvas.drawLine(
                cx - cxOffset,
                0f,
                cx - cxOffset,
                height,
                paintGrid
            )

            // Right
            canvas.drawLine(
                cx + cxOffset,
                0f,
                cx + cxOffset,
                height,
                paintGrid
            )

            // Step
            cxOffset += gridStep
            if (cx + cxOffset > width || cx - cxOffset < 0) break
        }
    }

    private fun drawY(canvas: Canvas) {

        val width = canvas.width.toFloat()
        val height = canvas.height.toFloat()

        val cy = height / 2
        var cyOffset = gridStep

        canvas.drawLine(
            0f,
            cy,
            width,
            cy,
            paintXY
        )

        while (true) {

            // Left
            canvas.drawLine(
                0f,
                cy + cyOffset,
                width,
                cy + cyOffset,
                paintGrid
            )

            // Right
            canvas.drawLine(
                0f,
                cy - cyOffset,
                width,
                cy - cyOffset,
                paintGrid
            )

            // Step
            cyOffset += gridStep
            if (cy + cyOffset > height || cy - cyOffset < 0) break
        }
    }

    private fun drawText(canvas: Canvas) {

        if (scale < 0.6f) return

        val cy = canvas.height / 2f
        val cx = canvas.width / 2f

        canvas.drawText(
            "0",
            cx + 20,
            cy + 40,
            paintText
        )

        var cxOffset = gridStep
        while (true) {
            canvas.drawText(
                (cxOffset / gridStep).roundToInt().toString(),
                cx + cxOffset + 5,
                cy + 40,
                paintText
            )

            cxOffset += gridStep
            if (cx + cxOffset > width || cx - cxOffset < 0) break
        }

        var cyOffset = gridStep
        while (true) {
            canvas.drawText(
                (cyOffset / gridStep).roundToInt().toString(),
                cx - 30,
                cy - cyOffset,
                paintText
            )

            cyOffset += gridStep
            if (cy + cyOffset > height || cy - cyOffset < 0) break
        }
    }
}