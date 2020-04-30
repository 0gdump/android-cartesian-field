package open.geosolve.canvasdemo.presentation

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import open.geosolve.geosolve.repository.model.Figure

@StateStrategyType(AddToEndSingleStrategy::class)
interface MainView : MvpView {
    fun attach(figure: Figure)
}