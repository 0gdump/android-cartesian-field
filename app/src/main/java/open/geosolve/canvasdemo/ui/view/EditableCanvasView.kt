package open.geosolve.canvasdemo.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import kotlin.math.abs

class EditableCanvasView : SimpleCanvasView {

    private var isMoved = false
    private var isFigureMoved = false

    private var lastX: Float = Float.NaN
    private var lastY: Float = Float.NaN

    var callback: FigureManipulationsCallback? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onTouchEvent(event: MotionEvent): Boolean {

        val mx = getXFromEvent(event)
        val my = getYFromEvent(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                savePosition(mx, my)
            }

            MotionEvent.ACTION_MOVE -> {
                if (isMoved(mx, my)) {

                    if (!isMoved) {
                        isFigureMoved = callback?.isUsed(lastX, lastY) ?: false
                        callScrollStartCallback(lastX, lastY)
                    } else {
                        callScrollCallback(mx, my)
                    }

                    savePosition(mx, my)
                    isMoved = true
                }
            }

            MotionEvent.ACTION_UP -> {
                when {
                    isMoved && isFigureMoved -> callback?.onMoveFinished(mx, my)
                    isMoved -> onScrollFinished(mx, my)
                    else -> callback?.onTouch(mx, my)
                }

                isMoved = false
                isFigureMoved = false
                lastX = Float.NaN
                lastY = Float.NaN
            }
        }

        invalidate()
        return true
    }

    private fun callScrollStartCallback(x: Float, y: Float) {
        if (isFigureMoved) {
            callback?.onMoveStart(x, y)
        } else {
            onScrollStart(x, y)
        }
    }

    private fun callScrollCallback(x: Float, y: Float) {
        if (isFigureMoved) {
            callback?.onMove(x, y)
        } else {
            onScroll(x, y)
        }
    }

    var startX = 0f
    var startY = 0f
    var startOX = 0f
    var startOY = 0f

    private fun onScrollStart(x: Float, y: Float) {
        startX = x
        startY = y
        startOX = xOffset
        startOY = yOffset
    }

    private fun onScroll(x: Float, y: Float) {
    }

    private fun onScrollFinished(x: Float, y: Float) {
        xOffset += -(x - startX)
        yOffset -= -(x - startX)
    }

    private fun savePosition(x: Float, y: Float) {
        lastX = x
        lastY = y
    }

    private fun isMoved(x: Float, y: Float) = lastX != x || lastY != y

    private fun getXFromEvent(event: MotionEvent) = roundCoordinate(fromAbsoluteX(event.x))
    private fun getYFromEvent(event: MotionEvent) = roundCoordinate(fromAbsoluteY(event.y))

    private fun roundCoordinate(coordinate: Float): Float {

        val decimalPart = abs(coordinate % 1)

        return when {
            decimalPart < 0.20f -> {
                if (coordinate > 0) {
                    coordinate - decimalPart
                } else {
                    coordinate + decimalPart
                }
            }
            decimalPart > 0.80f -> {
                if (coordinate > 0) {
                    coordinate + (1f - decimalPart)
                } else {
                    coordinate - (1f - decimalPart)
                }
            }

            else -> coordinate
        }
    }
}