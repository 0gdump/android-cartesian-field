package open.geosolve.canvasdemo.ui.view

import android.content.Context
import android.util.AttributeSet

class EditableCanvasView : SimpleCanvasView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

}