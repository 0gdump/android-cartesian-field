package open.v0gdump.field

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import kotlin.math.abs

abstract class InteractiveFieldView : BaseFieldView {

    private var lastX: Float = Float.NaN
    private var lastY: Float = Float.NaN

    private var isMoved = false
    private var isContentMoved = false

    var callback: InteractiveFieldCallback? = null

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
                        isContentMoved = callback?.isUsedByContent(lastX, lastY) ?: false
                        if (isContentMoved) callback?.onMoveStart(lastX, lastY)
                    } else {
                        if (isContentMoved) callback?.onMove(mx, my)
                    }

                    savePosition(mx, my)
                    isMoved = true
                }
            }

            MotionEvent.ACTION_UP -> {
                if (isMoved && isContentMoved) {
                    callback?.onMoveFinished(mx, my)
                } else {
                    callback?.onTouch(mx, my)
                }

                isMoved = false
                isContentMoved = false
                lastX = Float.NaN
                lastY = Float.NaN
            }
        }

        invalidate()
        return true
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