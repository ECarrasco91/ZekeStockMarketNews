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

    suspend fun getRealTimeQuote(symbol: String) = repository.getRealTimeQuote(symbol).asLiveData()

    suspend fun setCandleStickData(symbol: String, timestamp: Long) =
        repository.setCandleStickData(symbol, timestamp).asLiveData()
}