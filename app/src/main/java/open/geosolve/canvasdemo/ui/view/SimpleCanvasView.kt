package open.geosolve.canvasdemo.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import open.geosolve.canvasdemo.model.Point

open class SimpleCanvasView : View {

    companion object {
        private const val POINT_RADIUS: Float = 20f
        private const val GRID_STEP = 120
    }

    private val paintPoint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
    }

    private val paintXY = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        strokeWidth = 5f
    }

    private val paintGrid = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.LTGRAY
    }

    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 42f
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var attachedPoint: Point? = null

    fun attachPoint(point: Point?) {
        attachedPoint = point
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawGrid(canvas)
        drawPoint(canvas)
        drawText(canvas)
    }

    private fun drawPoint(canvas: Canvas) {
        if (attachedPoint == null) {
            canvas.drawColor(Color.WHITE)
        } else {
            val width = canvas.width.toFloat()
            val height = canvas.height.toFloat()

            val x = width / 2f + GRID_STEP * attachedPoint!!.x
            val y = height / 2f - GRID_STEP * attachedPoint!!.y

            canvas.drawCircle(x, y, POINT_RADIUS, paintPoint)
        }
    }

    private fun drawGrid(canvas: Canvas) {
        drawX(canvas)
        drawY(canvas)
    }

    private fun drawX(canvas: Canvas) {

        val width = canvas.width.toFloat()
        val height = canvas.height.toFloat()

        val cx = width / 2f
        var cxOffset = GRID_STEP

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
            cxOffset += GRID_STEP
            if (cx + cxOffset > width || cx - cxOffset < 0) break
        }
    }

    private fun drawY(canvas: Canvas) {

        val width = canvas.width.toFloat()
        val height = canvas.height.toFloat()

        val cy = height / 2
        var cyOffset = GRID_STEP

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
            cyOffset += GRID_STEP
            if (cy + cyOffset > height || cy - cyOffset < 0) break
        }
    }

    private fun drawText(canvas: Canvas) {

        val cy = canvas.height / 2f
        val cx = canvas.width / 2f

        var cxOffset = 0
        while (true) {
            canvas.drawText(
                (cxOffset / GRID_STEP).toString(),
                cx + cxOffset + 5,
                cy + 40,
                paintText
            )

            cxOffset += GRID_STEP
            if (cx + cxOffset > width || cx - cxOffset < 0) break
        }

        var cyOffset = GRID_STEP
        while (true) {
            canvas.drawText(
                (cyOffset / GRID_STEP).toString(),
                cx - 30,
                cy - cyOffset,
                paintText
            )

            cyOffset += GRID_STEP
            if (cy + cyOffset > height || cy - cyOffset < 0) break
        }
    }
}