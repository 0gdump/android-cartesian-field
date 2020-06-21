package open.v0gdump.field_demo.presentation

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import open.v0gdump.field_demo.model.Polygon

@StateStrategyType(AddToEndSingleStrategy::class)
interface MainView : MvpView {
    fun attach(polygon: Polygon)
    fun redraw()
}