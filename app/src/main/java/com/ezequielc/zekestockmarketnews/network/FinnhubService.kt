package com.ezequielc.zekestockmarketnews.network

import com.ezequielc.zekestockmarketnews.util.FINNHUB_TOKEN
import retrofit2.http.GET
import retrofit2.http.Query

interface FinnhubService {

    /**
     * https://finnhub.io/api/v1/quote?symbol={symbol}&token=YOUR_API_TOKEN
     */
    @GET("quote")
    suspend fun getRealTimeQuote(
        @Query("symbol") symbol: String,
        @Query("token") token: String = FINNHUB_TOKEN
    ): RealTimeQuoteResponse
}