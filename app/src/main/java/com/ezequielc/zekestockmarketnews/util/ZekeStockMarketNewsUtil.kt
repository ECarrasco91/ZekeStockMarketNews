package com.ezequielc.zekestockmarketnews.util

import android.content.Context
import android.content.res.Configuration
import androidx.core.content.ContextCompat
import com.ezequielc.zekestockmarketnews.data.ChartTimeframe
import com.ezequielc.zekestockmarketnews.data.ChartTimestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import kotlin.math.ln
import kotlin.math.pow

fun getString(context: Context, int: Int, args: String) = context.resources?.getString(int, args)

fun getColorInt(context: Context?, color: Int) = ContextCompat.getColor(context!!, color)

fun changeColorFromTheme(
    context: Context,
    lightThemeBlock: () -> Unit,
    darkThemeBlock: () -> Unit
) {
    val configuration = context.resources.configuration
    when (configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
        // Night mode is not active, we're using the light theme
        Configuration.UI_MODE_NIGHT_NO -> lightThemeBlock.invoke()
        // Night mode is active, we're using dark theme
        Configuration.UI_MODE_NIGHT_YES -> darkThemeBlock.invoke()
    }
}

fun convertToTimestamp(date: String) = Instant.parse(date).epochSecond

fun formatTimestamp(pattern: String, value: Long): String {
    val simpleDateFormat = SimpleDateFormat(pattern)
    val date = Date(value * 1000)
    return simpleDateFormat.format(date)
}

fun differentiateTimeframe(toTimestamp: Long, timeframe: String): ChartTimeframe {
    val interval: String

    val pattern = "MM.dd,yyyy-hh:mm:ss"
    val timestampFormatted = formatTimestamp(pattern, toTimestamp)

    val openTime = "-09:30:00"
    val split = timestampFormatted.split("-")
    val openTimeFormatted = split[0].plus(openTime)

    val simpleDateFormat = SimpleDateFormat(pattern)
    val date = simpleDateFormat.parse(openTimeFormatted)

    val calendar = Calendar.getInstance()
    calendar.time = date!!

    when (timeframe) {
        "5D" -> {
            interval = "30" // 30 minute interval
            calendar.add(Calendar.DAY_OF_YEAR, -6) // subtracting 6 days
        }
        "1M" -> {
            interval = "D" // 1 day interval
            calendar.add(Calendar.MONTH, -1) // minus one month
            calendar.add(Calendar.DAY_OF_YEAR, +1) // plus one day
        }
        "6M" -> {
            interval = "D" // 1 day interval
            calendar.add(Calendar.MONTH, -6) // minus six month
            calendar.add(Calendar.DAY_OF_YEAR, +1) // plus one day
        }
        "YTD" -> {
            interval = "D" // 1 day interval

            val januaryFirst = "01.01,"
            val newDate = openTimeFormatted.split(",")
            val ytdString = januaryFirst.plus(newDate[1])

            val ytdDate = simpleDateFormat.parse(ytdString)
            calendar.time = ytdDate!!

            val timestamp = ytdDate.time / 1000
            return ChartTimeframe(interval, timestamp)
        }
        "1Y" -> {
            interval = "D" // 1 day interval
            calendar.add(Calendar.YEAR, -1) // minus one month
            calendar.add(Calendar.DAY_OF_YEAR, +1) // plus one day
        }
        else -> { // "1D"
            interval = "1" // 1 minute interval
            val timestamp = date.time / 1000
            return ChartTimeframe(interval, timestamp)
        }
    }

    val newDate = calendar.time
    val timestamp = newDate.time / 1000
    return ChartTimeframe(interval, timestamp)
}

fun differentiateFormatTimestamp(timeframe: String, timestamp: Long): ChartTimestamp {
    val xAxisPattern: String = when (timeframe) {
        "1D" -> "h:mm a"
        "5D" -> "EE h:mm a"
        // for "1M", "6M", "YTD", "1Y"
        else -> "MMM d"
    }

    val candleStickPattern: String = when (timeframe) {
        "1D" -> "h:mm a"
        "5D" -> "EE MMM d h:mm a"
        // for "1M", "6M", "YTD", "1Y"
        else -> "MMM d yyyy"
    }

    val xAxis = formatTimestamp(xAxisPattern, timestamp)
    val candleStickTime = formatTimestamp(candleStickPattern, timestamp)
    return ChartTimestamp(xAxis, candleStickTime)
}

// Formats volume value with a metric prefix
// If the volume is greater than 1000, the appropriate metric prefix is appended to the volume
// e.g. 1K or 100.1K, 2M or 200.2M, 3B or 300.3B, etc.
fun formatVolume(volume: Long): String {
    if (volume < 1000) return "" + volume
    val exp = (ln(volume.toDouble()) / ln(1000.0)).toInt()
    return String.format("%.1f%c", volume / 1000.0.pow(exp.toDouble()), "KMBTQ"[exp - 1])
}