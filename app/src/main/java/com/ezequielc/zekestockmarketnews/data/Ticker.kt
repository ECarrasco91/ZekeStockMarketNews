package com.ezequielc.zekestockmarketnews.data

import android.os.Parcelable
import com.ezequielc.zekestockmarketnews.util.formatTimestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ticker(
    val symbol: String,
    val name: String,
    val exchange: String
) : Parcelable

data class TickerPrice(
    val currentPrice: Double,
    val dayChange: Double,
    val dayChangePercentage: Double,
    val timestamp: Long,
    val pattern: String = "MMM dd 'at' h:mm a"
) {
    val timestampFormatted: String
        get() = formatTimestamp(pattern, timestamp)
}
