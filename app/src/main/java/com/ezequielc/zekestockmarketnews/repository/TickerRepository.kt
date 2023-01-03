package com.ezequielc.zekestockmarketnews.repository

import com.ezequielc.zekestockmarketnews.data.TickerPrice
import com.ezequielc.zekestockmarketnews.network.FinnhubService
import com.ezequielc.zekestockmarketnews.util.Resource
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
}