package open.geosolve.canvasdemo.presentation

import moxy.MvpPresenter
import open.geosolve.canvasdemo.model.Point

class MainPresenter : MvpPresenter<MainView>() {

    private val point = Point().apply {
        x = 2f
        y = 3f
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.attachPoint(point)
    }
}