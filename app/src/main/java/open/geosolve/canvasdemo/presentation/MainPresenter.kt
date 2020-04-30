package open.geosolve.canvasdemo.presentation

import moxy.MvpPresenter
import open.geosolve.geosolve.repository.model.Figure

class MainPresenter : MvpPresenter<MainView>() {

    private val figure = Figure()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.attach(figure)
    }
}