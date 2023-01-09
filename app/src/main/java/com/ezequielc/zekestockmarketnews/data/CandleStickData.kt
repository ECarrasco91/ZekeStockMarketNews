package com.ezequielc.zekestockmarketnews.data

import com.github.mikephil.charting.data.CandleDataSet

data class CandleStickData(
    val index: Int,
    val timestamp: Long,
    val openPrice: Double,
    val highPrice: Double,
    val lowPrice:Double,
    val closePrice:Double,
    val volume: Long
)

data class CandleStickChartData(
    val candleDataSet: CandleDataSet,
    val xAxisValues: List<String>,
    val volumes: List<String>,
    val candleStickTime: List<String>,
)

data class ChartTimeframe(
    val interval: String,
    val timestamp: Long
)

data class ChartTimestamp(
    val xAxis: String,
    val candleStick: String
)