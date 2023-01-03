package com.ezequielc.zekestockmarketnews.network

import com.ezequielc.zekestockmarketnews.util.STOCK_DATA_TOKEN
import retrofit2.http.GET
import retrofit2.http.Query

interface StockDataService {

    /**
     * https://api.stockdata.org/v1/news/all?language=en&must_have_entities=true&
     * api_token=YOUR_API_TOKEN
     */
    @GET("news/all")
    suspend fun getLatestMarketNews(
        @Query("language") language: String = "en",
        @Query("must_have_entities") must_have_entities: Boolean = true,
        @Query("api_token") api_token: String = STOCK_DATA_TOKEN
    ): MarketNewsResponse

    /**
     * https://api.stockdata.org/v1/news/all?page={page}&search={search}&language=en&
     * must_have_entities=true&filter_entities=true&api_token=YOUR_API_TOKEN
     */
    @GET("news/all")
    suspend fun querySearchResults(
        @Query("page") page: Int,
        @Query("search") search: String,
        @Query("language") language: String = "en",
        @Query("must_have_entities") must_have_entities: Boolean = true,
        @Query("filter_entities") filter_entities: Boolean = true,
        @Query("api_token") api_token: String = STOCK_DATA_TOKEN
    ): MarketNewsResponse
}