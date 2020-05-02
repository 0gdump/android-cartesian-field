package open.geosolve.canvasdemo.ui.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import open.geosolve.canvasdemo.R
import open.geosolve.canvasdemo.model.Node
import open.geosolve.geosolve.repository.model.Figure
import kotlin.math.roundToInt

open class SimpleCanvasView : View {

    //region Internal classes and enums

    private enum class TextAnchor {
        TopLeft, TopCenter, TopRight,
        MiddleLeft, MiddleCenter, MiddleRight,
        BottomLeft, BottomCenter, BottomRight
    }

    //endregion

    //region Drawing data

    var scale = 1.0f
        private set

    var xOffset = 0f
        private set

    var yOffset = 0f
        private set

    val gridStep
        get() = 120 * scale

    val pointRadius
        get() = 20 * scale

    val lineThickness
        get() = 1 * scale


    //endregion

    //region Switches

    private var showGrid = true
    private var showAxis = true

    private var minScaleForNotations = 0.5f
    private var showZeroNotation = true
    private var showPositiveXNotations = true
    private var showNegativeXNotations = true
    private var showPositiveYNotations = true
    private var showNegativeYNotations = true

    //endregion

    //region Paints

    private val paintXY = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        strokeWidth = 5f
    }

    private val paintGrid = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.LTGRAY
    }

    private val paintNotations = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 42f
        typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
    }

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

    private var attachedFigure: Figure? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawGrid(canvas)
        drawFigure(canvas)
        drawNotations(canvas)
    }

    //region Cartesian drawing

    protected fun fromAbsoluteX(x: Float) = (x - width / 2f) / gridStep + xOffset
    protected fun fromAbsoluteY(y: Float) = (height / 2f - y) / gridStep + yOffset

    private fun toAbsoluteX(x: Float) = width / 2f + (x - xOffset) * gridStep
    private fun toAbsoluteY(y: Float) = height / 2f - (y - yOffset) * gridStep

    private fun isXonScreen(x: Float) = toAbsoluteX(x).roundToInt() in 0..width
    private fun isYonScreen(y: Float) = toAbsoluteY(y).roundToInt() in 0..height

    private fun getXOnScreen(): IntRange {
        val cellsPerHalf = (width / 2 / gridStep).roundToInt()

        val minX = -cellsPerHalf + xOffset.roundToInt()
        val maxX = cellsPerHalf + xOffset.roundToInt()

        return minX..maxX
    }

    private fun getYOnScreen(): IntRange {
        val cellsPerHalf = (height / 2 / gridStep).roundToInt()

        val minY = -cellsPerHalf + yOffset.roundToInt()
        val maxY = cellsPerHalf + yOffset.roundToInt()

        return minY..maxY
    }

    private fun drawCircle(
        c: Canvas,
        x: Float,
        y: Float,
        r: Float,
        p: Paint
    ) {
        c.drawCircle(
            toAbsoluteX(x),
            toAbsoluteY(y),
            r,
            p
        )
    }

    private fun drawLine(
        c: Canvas,
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float,
        p: Paint
    ) {
        c.drawLine(
            toAbsoluteX(x1),
            toAbsoluteY(y1),
            toAbsoluteX(x2),
            toAbsoluteY(y2),
            p
        )
    }

    private fun drawEndlessVerticalLine(
        c: Canvas,
        x: Float,
        p: Paint
    ) {
        c.drawLine(
            toAbsoluteX(x),
            0f,
            toAbsoluteX(x),
            height.toFloat(),
            p
        )
    }

    private fun drawEndlessHorizontalLine(
        c: Canvas,
        y: Float,
        p: Paint
    ) {
        c.drawLine(
            0f,
            toAbsoluteY(y),
            width.toFloat(),
            toAbsoluteY(y),
            p
        )
    }

    private fun drawText(
        t: String,
        c: Canvas,
        x: Float,
        y: Float,
        p: Paint,
        a: TextAnchor = TextAnchor.MiddleCenter,
        safetyZone: Float = 0f
    ) {
        val bounds = Rect()
        p.getTextBounds(t, 0, t.length, bounds)

        val h = bounds.height().toFloat()
        val w = bounds.width().toFloat()

        var aox = 0f
        var aoy = 0f

        when (a) {
            TextAnchor.TopLeft -> {
                aox = 0f
                aoy = h
            }
            TextAnchor.TopCenter -> {
                aox = w / 2
                aoy = h
            }
            TextAnchor.TopRight -> {
                aox = w
                aoy = h
            }
            TextAnchor.MiddleLeft -> {
                aox = 0f
                aoy = h / 2
            }
            TextAnchor.MiddleCenter -> {
                aox = w / 2
                aoy = h / 2
            }
            TextAnchor.MiddleRight -> {
                aox = w
                aoy = h / 2
            }
            TextAnchor.BottomLeft -> {
                aox = 0f
                aoy = 0f
            }
            TextAnchor.BottomCenter -> {
                aox = w / 2
                aoy = 0f
            }
            TextAnchor.BottomRight -> {
                aox = w
                aoy = 0f
            }
        }

        c.drawText(
            t,
            toAbsoluteX(x) - aox + safetyZone,
            toAbsoluteY(y) + aoy + safetyZone,
            p
        )
    }

    //endregion

    //region Grid drawing

    private fun drawGrid(canvas: Canvas) {
        if (showGrid) {
            drawX(canvas)
            drawY(canvas)
        }

        if (showAxis) drawAxis(canvas)
    }

    private fun drawX(canvas: Canvas) {
        for (y in getYOnScreen()) {
            drawEndlessHorizontalLine(canvas, y.toFloat(), paintGrid)
        }
    }

    private fun drawY(canvas: Canvas) {
        for (x in getXOnScreen()) {
            drawEndlessVerticalLine(canvas, x.toFloat(), paintGrid)
        }
    }

    private fun drawAxis(canvas: Canvas) {
        drawEndlessHorizontalLine(canvas, 0f, paintXY)
        drawEndlessVerticalLine(canvas, 0f, paintXY)
    }

    private fun drawNotations(canvas: Canvas) {

        if (scale < minScaleForNotations) return

        if (showZeroNotation) drawZero(canvas)
        if (showPositiveXNotations || showNegativeXNotations) drawXNotations(canvas)
        if (showPositiveYNotations || showNegativeYNotations) drawYNotations(canvas)
    }

    private fun drawZero(canvas: Canvas) {
        drawText(
            t = "0",
            c = canvas,
            x = 0f,
            y = 0f,
            p = paintNotations,
            a = TextAnchor.TopLeft,
            safetyZone = 12f
        )
    }

    private fun drawXNotations(canvas: Canvas) {
        loop@ for (x in getXOnScreen()) {

            when {
                x < 0 && !showNegativeXNotations -> continue@loop
                x > 0 && !showPositiveXNotations -> continue@loop
                x == 0 -> continue@loop
            }

            drawText(
                t = x.toString(),
                c = canvas,
                x = x.toFloat(),
                y = 0f,
                p = paintNotations,
                a = TextAnchor.TopLeft,
                safetyZone = 12f
            )
        }
    }

    private fun drawYNotations(canvas: Canvas) {
        loop@ for (y in getYOnScreen()) {

            when {
                y < 0 && !showNegativeYNotations -> continue@loop
                y > 0 && !showPositiveYNotations -> continue@loop
                y == 0 -> continue@loop
            }

            drawText(
                t = y.toString(),
                c = canvas,
                x = 0f,
                y = y.toFloat(),
                p = paintNotations,
                a = TextAnchor.TopLeft,
                safetyZone = 12f
            )
        }
    }

    //endregion

    //region Figure drawing

    private fun drawFigure(canvas: Canvas) {
        if (attachedFigure != null) {
            drawLines(canvas)
            drawNodes(canvas)
        }
    }

    private fun drawLines(canvas: Canvas) {

        if (attachedFigure == null) return

        for (line in attachedFigure!!.lines) {
            drawLine(
                canvas,
                line.startNode.x, line.startNode.y,
                line.finalNode.x, line.finalNode.y,
                paintLine
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
        drawCircle(canvas, node.x, node.y, pointRadius, paintNode)
    }

    private fun drawAngleDecoration(canvas: Canvas, node: Node) {
        if (node.innerAngle == null) return

        drawText(
            node.innerAngle.toString(),
            canvas,
            node.x + 50,
            node.y + 50,
            paintAngle
        )
    }

    //endregion

    fun attach(figure: Figure?) {
        attachedFigure = figure
    }

    fun updateScale(scale: Float) {
        this.scale = scale

        // TODO Update paints options

        invalidate()
    }

    fun updateOffset(xOffset: Float, yOffset: Float) {
        this.xOffset = xOffset
        this.yOffset = yOffset

        invalidate()
    }
}