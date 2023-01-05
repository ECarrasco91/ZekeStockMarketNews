package com.ezequielc.zekestockmarketnews.network

import com.ezequielc.zekestockmarketnews.data.CandleStickData
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CandleStickDataResponse(
    @Json(name = "c")
    val closePrices: List<Double>,
    @Json(name = "h")
    val highPrices: List<Double>,
    @Json(name = "l")
    val lowPrices: List<Double>,
    @Json(name = "o")
    val openPrices: List<Double>,
    @Json(name = "s")
    val status: String,
    @Json(name = "t")
    val timestamps: List<Long>,
    @Json(name = "v")
    val volumes: List<Long>
) {

    fun asModel(): List<CandleStickData> {
        val data = mutableListOf<CandleStickData>()

        for (i in timestamps.indices) {
            val candleStickData = CandleStickData(
                index = i,
                timestamp = timestamps[i],
                openPrice = openPrices[i],
                highPrice = highPrices[i],
                lowPrice = lowPrices[i],
                closePrice = closePrices[i],
                volume = volumes[i],
            )

            data.add(candleStickData)
        }

        return data
    }
}