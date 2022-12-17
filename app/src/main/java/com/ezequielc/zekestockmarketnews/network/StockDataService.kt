package com.ezequielc.zekestockmarketnews.network

import com.ezequielc.zekestockmarketnews.util.STOCK_DATA_TOKEN
import retrofit2.http.GET
import retrofit2.http.Query

interface StockDataService {

    /**
     * https://api.stockdata.org/v1/news/all?language=en&api_token=YOUR_API_TOKEN
     */
    @GET("news/all")
    suspend fun getLatestMarketNews(
        @Query("language") language: String = "en",
        @Query("api_token") api_token: String = STOCK_DATA_TOKEN
    ): MarketNewsResponse
}