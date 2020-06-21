package open.v0gdump.field_demo.ui

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import open.v0gdump.field.InteractiveFieldCallback
import open.v0gdump.field_demo.R
import open.v0gdump.field_demo.model.Polygon
import open.v0gdump.field_demo.presentation.MainPresenter
import open.v0gdump.field_demo.presentation.MainView

class MainActivity : MvpAppCompatActivity(), MainView {

    private val presenter by moxyPresenter { MainPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        field.callback = object : InteractiveFieldCallback {

            override fun isBelongToContent(x: Float, y: Float): Boolean =
                presenter.isUsedByContent(x, y)

            override fun onMoveStart(x: Float, y: Float) =
                presenter.onMoveStart(x, y)

            override fun onMove(x: Float, y: Float) =
                presenter.onMove(x, y)

            override fun onMoveFinished(x: Float, y: Float) =
                presenter.onMoveFinished(x, y)

            override fun onTouchContent(x: Float, y: Float) =
                presenter.onTouchContent(x, y)

            override fun onTouchField(x: Float, y: Float) =
                presenter.onTouchField(x, y)

            override fun onAnyTouch(x: Float, y: Float) =
                presenter.onAnyTouch(x, y)
        }
    }

    override fun attach(polygon: Polygon) {
        field.attach(polygon)
    }

    override fun redraw() {
        field.invalidate()
    }
}
