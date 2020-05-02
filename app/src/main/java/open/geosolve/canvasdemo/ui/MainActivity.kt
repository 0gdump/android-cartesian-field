package open.geosolve.canvasdemo.ui

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import open.geosolve.canvasdemo.R
import open.geosolve.canvasdemo.presentation.MainPresenter
import open.geosolve.canvasdemo.presentation.MainView
import open.geosolve.canvasdemo.ui.view.FigureManipulationsCallback
import open.geosolve.geosolve.repository.model.Figure

class MainActivity : MvpAppCompatActivity(), MainView {

    private val presenter by moxyPresenter { MainPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        canvas.callback = object : FigureManipulationsCallback {

            override fun isUsed(x: Float, y: Float): Boolean =
                presenter.isUsed(x, y)

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
        canvas.attach(figure)
    }
}
