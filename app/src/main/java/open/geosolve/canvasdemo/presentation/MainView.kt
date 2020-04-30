package open.geosolve.canvasdemo.presentation

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import open.geosolve.canvasdemo.model.Point

@StateStrategyType(AddToEndSingleStrategy::class)
interface MainView : MvpView {
    fun attachPoint(point: Point)
}