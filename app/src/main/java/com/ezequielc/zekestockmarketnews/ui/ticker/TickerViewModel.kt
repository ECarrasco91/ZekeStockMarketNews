package com.ezequielc.zekestockmarketnews.ui.ticker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ezequielc.zekestockmarketnews.repository.TickerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TickerViewModel @Inject constructor(
    private val repository: TickerRepository
) : ViewModel() {

    private var _symbol: String? = null
    val symbol get() = _symbol

    private var _timestamp: Long? = null
    val timestamp get() = _timestamp

    fun setSymbol(symbol: String) {
        _symbol = symbol
    }

    fun setTimestamp(timestamp: Long) {
        _timestamp = timestamp
    }

    suspend fun getRealTimeQuote(symbol: String) = repository.getRealTimeQuote(symbol).asLiveData()

    suspend fun setCandleStickData(symbol: String, timestamp: Long, timeframe: String) =
        repository.setCandleStickData(symbol, timestamp, timeframe).asLiveData()
}