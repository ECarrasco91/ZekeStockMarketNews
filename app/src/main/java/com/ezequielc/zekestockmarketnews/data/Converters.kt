package com.ezequielc.zekestockmarketnews.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Converters {

    @TypeConverter
    fun fromNewsArticle(newsArticle: NewsArticle): String? {
        return Gson().toJson(newsArticle)
    }

    @TypeConverter
    fun toNewsArticle(newsArticle: String): NewsArticle? {
        val newsArticleType = object : TypeToken<NewsArticle>() {}.type
        return Gson().fromJson(newsArticle, newsArticleType)
    }

    @TypeConverter
    fun fromTickerList(tickers: List<Ticker>): String? {
        return Gson().toJson(tickers)
    }

    @TypeConverter
    fun toTickerList(tickers: String): List<Ticker>? {
        val tickerType = object : TypeToken<List<Ticker>>() {}.type
        return Gson().fromJson(tickers, tickerType)
    }
}