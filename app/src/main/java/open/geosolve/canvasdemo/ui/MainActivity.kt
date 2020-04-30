package open.geosolve.canvasdemo.ui

import android.os.Bundle
import android.widget.SeekBar
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

        updateScaleFromSeekbar(0f)

        scale.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateScaleFromSeekbar(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        canvas.onTouchUp = { x, y -> presenter.onTouchUp(x, y) }
        canvas.onTouchDown = { x, y -> presenter.onTouchDown(x, y) }
        canvas.onTouchMove = { x, y -> presenter.onTouchMove(x, y) }
    }

    private fun updateScaleFromSeekbar(progress: Float) {
        canvas.updateScale(
            mapValueToRange(
                progress,
                0f,
                100f,
                0.2f,
                10f
            )
        )
    }

    private fun mapValueToRange(
        x: Float,
        inMin: Float,
        inMax: Float,
        outMin: Float,
        outMax: Float
    ): Float {
        return (x - inMin) * (outMax - outMin) / (inMax - inMin) + outMin
    }

    override fun attach(figure: Figure) {
        canvas.attach(figure)
    }
}
