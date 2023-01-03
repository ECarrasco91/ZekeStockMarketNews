package com.ezequielc.zekestockmarketnews.di

import com.ezequielc.zekestockmarketnews.network.FinnhubService
import com.ezequielc.zekestockmarketnews.network.StockDataService
import com.ezequielc.zekestockmarketnews.util.FINNHUB_BASE_URL
import com.ezequielc.zekestockmarketnews.util.STOCK_DATA_BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi() = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    @Singleton
    fun provideStockDataService(moshi: Moshi) = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(STOCK_DATA_BASE_URL)
        .build()
        .create(StockDataService::class.java)

    @Provides
    @Singleton
    fun provideFinnhubService(moshi: Moshi) = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(FINNHUB_BASE_URL)
        .build()
        .create(FinnhubService::class.java)
}