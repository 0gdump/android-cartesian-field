package open.v0gdump.field

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import kotlin.math.roundToInt

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseFieldView : View {

    //region Drawing data

    val cx
        get() = width / 2f

    val cy
        get() = height / 2f

    var scale = 1f
        protected set

    var xOffset = 0f
        protected set

    var yOffset = 0f
        protected set

    val gridStep
        get() = dp(64 * scale)

    val scaledTextSize
        get() = sp(16 * scale)

    //endregion

    //region Paints

    protected val paintAxis = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        strokeWidth = dp(2 * scale)
    }

    protected val paintGrid = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.LTGRAY
    }

    protected val paintNotations = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = scaledTextSize
        typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
    }

    //endregion

    //region Options and switchers

    var showGrid = true
    var showAxis = true

    var minScaleForNotations = 0.5f
    var showZeroNotation = true
    var showPositiveXNotations = true
    var showNegativeXNotations = true
    var showPositiveYNotations = true
    var showNegativeYNotations = true

    //endregion

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
        drawNotations(canvas)
    }

    //region Helpers

    protected fun fromAbsoluteX(x: Float) = (x - cx) / gridStep + xOffset
    protected fun fromAbsoluteY(y: Float) = (cy - y) / gridStep + yOffset

    protected fun toAbsoluteX(x: Float) = cx + (x - xOffset) * gridStep
    protected fun toAbsoluteY(y: Float) = cy - (y - yOffset) * gridStep

    protected fun <T> dp(dp: T) where T : Number =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        )

    protected fun <T> sp(sp: T) where T : Number =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp.toFloat(),
            resources.displayMetrics
        )

    protected fun <T> dc(dc: T) where T : Number =
        gridStep * dc.toFloat()

    protected fun getXOnScreen(): IntRange {
        val cellsPerHalf = (cx / gridStep).roundToInt()

        val minX = -cellsPerHalf + xOffset.roundToInt()
        val maxX = cellsPerHalf + xOffset.roundToInt()

        return minX..maxX
    }

    protected fun getYOnScreen(): IntRange {
        val cellsPerHalf = (cy / gridStep).roundToInt()

        val minY = -cellsPerHalf + yOffset.roundToInt()
        val maxY = cellsPerHalf + yOffset.roundToInt()

        return minY..maxY
    }

    protected fun isXonScreen(x: Float) = toAbsoluteX(x).roundToInt() in getXOnScreen()
    protected fun isYonScreen(y: Float) = toAbsoluteY(y).roundToInt() in getYOnScreen()

    //endregion

    //region Basic drawing

    protected fun drawCircle(
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

    protected fun drawLine(
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

    protected fun drawEndlessVerticalLine(
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

    protected fun drawEndlessHorizontalLine(
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

    protected fun drawText(
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

        val h = bounds.height().toFloat() + safetyZone
        val w = bounds.width().toFloat() + safetyZone

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
            toAbsoluteX(x) - aox,
            toAbsoluteY(y) + aoy,
            p
        )
    }

    //endregion

    protected fun drawGrid(canvas: Canvas) {

        if (showGrid) {
            drawAxesX(canvas)
            drawAxesY(canvas)
        }

        if (showAxis) {
            drawBaselines(canvas)
        }
    }

    protected fun drawBaselines(canvas: Canvas) {
        drawEndlessHorizontalLine(canvas, 0f, paintAxis)
        drawEndlessVerticalLine(canvas, 0f, paintAxis)
    }

    protected fun drawAxesX(canvas: Canvas) {
        for (y in getYOnScreen()) {
            drawEndlessHorizontalLine(canvas, y.toFloat(), paintGrid)
        }
    }

    protected fun drawAxesY(canvas: Canvas) {
        for (x in getXOnScreen()) {
            drawEndlessVerticalLine(canvas, x.toFloat(), paintGrid)
        }
    }

    protected fun drawNotations(canvas: Canvas) {

        if (scale < minScaleForNotations) return

        if (showZeroNotation) drawZero(canvas)
        if (showPositiveXNotations || showNegativeXNotations) drawXNotations(canvas)
        if (showPositiveYNotations || showNegativeYNotations) drawYNotations(canvas)
    }

    protected fun drawZero(canvas: Canvas) {
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

    protected fun drawXNotations(canvas: Canvas) {
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

    protected fun drawYNotations(canvas: Canvas) {
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
}