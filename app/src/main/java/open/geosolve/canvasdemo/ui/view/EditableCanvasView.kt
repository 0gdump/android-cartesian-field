package open.geosolve.canvasdemo.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import kotlin.math.abs

class EditableCanvasView : SimpleCanvasView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    var onTouchDown: ((x: Float, y: Float) -> Unit)? = null
    var onTouchMove: ((x: Float, y: Float) -> Unit)? = null
    var onTouchUp: ((x: Float, y: Float) -> Unit)? = null

    override fun onTouchEvent(event: MotionEvent): Boolean {

        val width = width.toFloat()
        val height = height.toFloat()

        val cx = width / 2
        val cy = height / 2

        val mx = roundCoordinate((event.x - cx) / gridStep)
        val my = roundCoordinate((cy - event.y) / gridStep)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> onTouchDown?.invoke(mx, my)
            MotionEvent.ACTION_MOVE -> onTouchMove?.invoke(mx, my)
            MotionEvent.ACTION_UP -> onTouchUp?.invoke(mx, my)
        }

        invalidate()
        return true
    }

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