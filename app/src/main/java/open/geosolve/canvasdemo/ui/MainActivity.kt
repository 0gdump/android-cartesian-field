package open.geosolve.canvasdemo.ui

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import open.geosolve.canvasdemo.R
import open.geosolve.canvasdemo.presentation.MainPresenter
import open.geosolve.canvasdemo.presentation.MainView
import open.geosolve.geosolve.repository.model.Figure

class MainActivity : MvpAppCompatActivity(), MainView {

    private val presenter by moxyPresenter { MainPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        canvas.onTouchUp = { x, y -> presenter.onTouchUp(x, y) }
        canvas.onTouchDown = { x, y -> presenter.onTouchDown(x, y) }
        canvas.onTouchMove = { x, y -> presenter.onTouchMove(x, y) }
    }

    override fun attach(figure: Figure) {
        canvas.attach(figure)
    }
}
