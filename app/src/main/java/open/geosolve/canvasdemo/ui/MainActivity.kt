package open.geosolve.canvasdemo.ui

import android.os.Bundle
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import open.geosolve.canvasdemo.model.Point
import open.geosolve.canvasdemo.presentation.MainPresenter
import open.geosolve.canvasdemo.presentation.MainView
import open.geosolve.canvasdemo.ui.view.EditableCanvasView

class MainActivity : MvpAppCompatActivity(), MainView {

    private val presenter by moxyPresenter { MainPresenter() }
    private lateinit var content: EditableCanvasView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        content = EditableCanvasView(this)

        setContentView(content)
    }

    override fun attachPoint(point: Point) {
        content.attachPoint(point)
    }
}
