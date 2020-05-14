package open.v0gdump.field_demo.ui

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import open.v0gdump.field.InteractiveFieldCallback
import open.v0gdump.field_demo.R
import open.v0gdump.field_demo.model.Figure
import open.v0gdump.field_demo.presentation.MainPresenter
import open.v0gdump.field_demo.presentation.MainView

class MainActivity : MvpAppCompatActivity(), MainView {

    private val presenter by moxyPresenter { MainPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        field.callback = object : InteractiveFieldCallback {

            override fun isUsedByContent(x: Float, y: Float): Boolean =
                presenter.isUsedByContent(x, y)

            override fun onMoveStart(x: Float, y: Float) =
                presenter.onMoveStart(x, y)

            override fun onMove(x: Float, y: Float) =
                presenter.onMove(x, y)

            override fun onMoveFinished(x: Float, y: Float) =
                presenter.onMoveFinished(x, y)

            override fun onTouch(x: Float, y: Float) =
                presenter.onTouch(x, y)
        }
    }

    override fun attach(figure: Figure) {
        field.attach(figure)
    }

    override fun redraw() {
        field.invalidate()
    }
}
