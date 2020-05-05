package open.v0gdump.field_demo.presentation

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import open.v0gdump.geosolve.repository.model.Figure

@StateStrategyType(AddToEndSingleStrategy::class)
interface MainView : MvpView {
    fun attach(figure: Figure)
    fun redraw()
}