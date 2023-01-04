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

    /**
     * https://finnhub.io/api/v1/stock/candle?symbol={symbol}&resolution={resolution}&from={from}&
     * to={to}&token=YOUR_API_TOKEN
     */
    @GET("stock/candle")
    suspend fun getCandleStickData(
        @Query("symbol") symbol: String,
        @Query("resolution") resolution: String,
        @Query("from") from: Long,
        @Query("to") to: Long,
        @Query("token") token: String = FINNHUB_TOKEN
    ): CandleStickDataResponse
}