package com.ezequielc.zekestockmarketnews.repository

import com.ezequielc.zekestockmarketnews.data.CandleStickChartData
import com.ezequielc.zekestockmarketnews.data.CandleStickData
import com.ezequielc.zekestockmarketnews.data.TickerPrice
import com.ezequielc.zekestockmarketnews.network.FinnhubService
import com.ezequielc.zekestockmarketnews.util.*
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TickerRepository @Inject constructor(
    private val service: FinnhubService
) {

    suspend fun getRealTimeQuote(symbol: String): Flow<Resource<TickerPrice>> {
        return flow {
            try {
                val response = service.getRealTimeQuote(symbol)
                val tickerPrice = response.asModel()
                emit(Resource.Success(tickerPrice))
            } catch (exception: Exception) {
                emit(Resource.Error(error = exception.toString()))
            }
        }
    }

    private suspend fun getCandleStickData(
        symbol: String,
        toTimestamp: Long,
    ): Flow<Resource<List<CandleStickData>>> {
        return flow {
            try {
                val result = calculateTimeframe(toTimestamp)
                val response = service.getCandleStickData(
                    symbol,
                    result.interval,
                    result.timestamp,
                    toTimestamp
                )

                val candleStickData = response.asModel()
                emit(Resource.Success(candleStickData))
            } catch (exception: Exception) {
                emit(Resource.Error(error = exception.toString()))
            }
        }
    }

    suspend fun setCandleStickData(
        symbol: String,
        timestamp: Long,
    ): Flow<Resource<CandleStickChartData>> {
        return flow {
            // X-axis Values
            val xAxisValues = mutableListOf<String>()

            // CandleStick Values
            val candleStickEntries = mutableListOf<CandleEntry>()

            // Volume Values for each CandleStick
            val volumeEntries = mutableListOf<String>()

            // Time Values for each CandleStick
            val timeEntries = mutableListOf<String>()

            val response = getCandleStickData(symbol, timestamp)
            val candleStickDataList = handleResponse(response)
            candleStickDataList.forEach { candleStickData ->
                // Adding X-axis Values and Time Values for each CandleStick
                val pattern = "h:mm a"
                val formattedValues = formatTimestamp(pattern, candleStickData.timestamp)
                xAxisValues.add(formattedValues)
                timeEntries.add(formattedValues)

                // Adding Volumes
                val formattedVolumes = formatVolume(candleStickData.volume)
                volumeEntries.add(formattedVolumes)

                // Adding CandleStick Values
                candleStickEntries.add(
                    CandleEntry(
                        candleStickData.index.toFloat(),
                        candleStickData.highPrice.toFloat(),
                        candleStickData.lowPrice.toFloat(),
                        candleStickData.openPrice.toFloat(),
                        candleStickData.closePrice.toFloat()
                    )
                )
            }

            try {
                if (candleStickEntries.isEmpty()) throw Exception()

                val candleDataSet = CandleDataSet(candleStickEntries, CHART_LABEL)
                val candleStickChartData =
                    CandleStickChartData(candleDataSet, xAxisValues, volumeEntries, timeEntries)
                emit(Resource.Success(candleStickChartData))
            } catch (exception: Exception) {
                emit(Resource.Error(error = exception.toString()))
            }
        }
    }

    private suspend fun handleResponse(
        data: Flow<Resource<List<CandleStickData>>>
    ): List<CandleStickData> {
        var tempList = emptyList<CandleStickData>()
        data.collect { resource ->
            if (resource is Resource.Success) {
                tempList = resource.data!!
            }
        }
        return tempList
    }
}