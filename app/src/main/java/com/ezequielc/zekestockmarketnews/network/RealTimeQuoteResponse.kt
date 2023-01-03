package com.ezequielc.zekestockmarketnews.network

import com.ezequielc.zekestockmarketnews.data.TickerPrice
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RealTimeQuoteResponse(
    @Json(name = "c")
    val currentPrice: Double,
    @Json(name = "d")
    val dayChange: Double,
    @Json(name = "dp")
    val dayChangePercentage: Double,
    @Json(name = "t")
    val timestamp: Long
) {

    fun asModel(): TickerPrice {
        return TickerPrice(
            currentPrice.toString(),
            dayChange.toString(),
            dayChangePercentage.toString(),
            timestamp
        )
    }
}
