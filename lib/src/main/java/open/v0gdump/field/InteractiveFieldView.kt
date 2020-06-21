package open.v0gdump.field

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import kotlin.math.abs

abstract class InteractiveFieldView : BaseFieldView {

    var callback: InteractiveFieldCallback? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onTouchEvent(event: MotionEvent): Boolean {

        // All logic inside listeners!

        // Handle scale
        scaleGestureDetector.onTouchEvent(event)

        // Scaling  can be considered complete even when the fingers are very close together.
        // Handles the end of zooming only after raising fingers
        if (scaleGestureDetector.isInProgress) {
            scaleEnd = false
            return true
        } else if (!scaleEnd && event.action == MotionEvent.ACTION_UP) {
            scaleEnd = true
            return true
        }

        // Handle other gestures
        gestureDetector.onTouchEvent(event)

        // Handle touch up
        if (event.action == MotionEvent.ACTION_UP)
            handleScrollUp(event)

        return true
    }

    private fun handleScrollUp(event: MotionEvent) {

        val mx = getXFromEvent(event)
        val my = getYFromEvent(event)

        if (isMoved && isContentMoved)
            callback?.onMoveFinished(mx, my)

        isMoved = false
        isContentMoved = false
    }

    //region Scroll

    private var scaleEnd = true
    private val scaleGestureDetector = ScaleGestureDetector(
        context,
        object : ScaleGestureDetector.OnScaleGestureListener {

            /* Do nothing */
            override fun onScaleBegin(detector: ScaleGestureDetector?) = true

            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                scale(
                    detector?.scaleFactor ?: throw RuntimeException("ScaleGestureDetector is null")
                )
                return true
            }

            /* Do nothing. Scaling end is captured in onTouchEvent */
            override fun onScaleEnd(detector: ScaleGestureDetector?) {}
        }
    )

    private fun scale(scaleFactor: Float) {
        scale *= scaleFactor
        invalidate()
    }

    //endregion

    //region Gesture detector

    private var isMoved = false
    private var isContentMoved = false

    private val gestureDetector = GestureDetector(
        context,
        object : GestureDetector.OnGestureListener {

            /* Do nothing */
            override fun onShowPress(e: MotionEvent?) {}

            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                onSingleTap(e ?: throw RuntimeException("MotionEvent is null"))
                return true
            }

            /* Do nothing */
            override fun onDown(e: MotionEvent?) = true

            /* Todo */
            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent?,
                velocityX: Float,
                velocityY: Float
            ) = true

            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent?,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                check(e1 != null && e2 != null) { "MotionEvent's can't be null" }
                this@InteractiveFieldView.onScroll(e1, e2, distanceX, distanceY)
                return true
            }

            override fun onLongPress(e: MotionEvent?) {}
        }
    )

    private fun onSingleTap(event: MotionEvent) {

        val mx = getXFromEvent(event)
        val my = getYFromEvent(event)

        val isTouchContent = callback?.isBelongToContent(mx, my) ?: false
        if (isTouchContent) {
            callback?.onTouchContent(mx, my)
        } else {
            callback?.onTouchField(mx, my)
        }
        callback?.onAnyTouch(mx, my)

        invalidate()
    }

    private fun onScroll(
        initialTouchEvent: MotionEvent,
        currentTouchEvent: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ) {

        if (!isContentMoved && isMoved) {
            scrollField(distanceX, distanceY)
        } else if (!isMoved && !isContentMoved) {
            onFirstScroll(initialTouchEvent)
        } else {
            onScroll(currentTouchEvent)
        }

        invalidate()
    }

    private fun onFirstScroll(initialTouchEvent: MotionEvent) {

        val fmx = getXFromEvent(initialTouchEvent)
        val fmy = getYFromEvent(initialTouchEvent)

        isContentMoved = callback?.isBelongToContent(fmx, fmy) ?: false

        if (isContentMoved) {
            callback?.onMoveStart(fmx, fmy)
        }

        isMoved = true
    }

    private fun onScroll(currentTouchEvent: MotionEvent) {

        val mx = getXFromEvent(currentTouchEvent)
        val my = getYFromEvent(currentTouchEvent)

        if (isContentMoved) {
            callback?.onMove(mx, my)
        }
    }

    //endregion

    private fun scrollField(distanceX: Float, distanceY: Float) {
        xOffset += distanceX / gridStep
        yOffset -= distanceY / gridStep
    }

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