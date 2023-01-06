package com.ezequielc.zekestockmarketnews.ui.chart

import android.content.Context
import android.graphics.Paint
import com.ezequielc.zekestockmarketnews.R
import com.ezequielc.zekestockmarketnews.data.CandleStickChartData
import com.ezequielc.zekestockmarketnews.util.getColorInt
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class CustomCandleStickChart(private val context: Context) {

    fun styleChart(
        candleStickChart: CandleStickChart,
        candleStickChartData: CandleStickChartData
    ) = candleStickChart.apply {
        legend.isEnabled = false
        description.isEnabled = false
        axisRight.isEnabled = false
        axisLeft.setDrawGridLines(false)

        xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            valueFormatter = IndexAxisValueFormatter(candleStickChartData.xAxisValues)
            setDrawGridLines(false)
        }

        val customMarkView = CustomMarkView(
            context,
            R.layout.custom_marker_view_layout,
            candleStickChartData.candleStickTime,
            candleStickChartData.volumes
        )

        customMarkView.chartView = this
        marker = customMarkView
        data = CandleData(candleStickChartData.candleDataSet)
        data.setDrawValues(false) // removes values shown above each entry
        data.notifyDataChanged()
        notifyDataSetChanged()
        invalidate()
    }

    fun styleCandleDataSet(candleDataSet: CandleDataSet) = candleDataSet.apply {
        candleDataSet.highLightColor = getColorInt(context, R.color.black)

        candleDataSet.shadowColor = getColorInt(context, R.color.darker_gray)
        candleDataSet.shadowWidth = 0.7f

        candleDataSet.decreasingColor = getColorInt(context, R.color.red)
        candleDataSet.decreasingPaintStyle = Paint.Style.FILL

        candleDataSet.increasingColor = getColorInt(context, R.color.green)
        candleDataSet.increasingPaintStyle = Paint.Style.FILL
    }
}