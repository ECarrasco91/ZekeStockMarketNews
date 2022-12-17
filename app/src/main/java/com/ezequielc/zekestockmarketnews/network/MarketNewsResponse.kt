package com.ezequielc.zekestockmarketnews.network

import com.ezequielc.zekestockmarketnews.data.NewsArticle
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MarketNewsResponse(
    val data: List<Data>
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        val uuid: String,
        val title: String,
        val description: String,
        val url: String,
        @Json(name = "image_url")
        val imageUrl: String,
        @Json(name = "published_at")
        val publishedAt: String,
        val source: String
    )

    fun asModel(): List<NewsArticle> {
        return data.map {
            NewsArticle(
                uuid = it.uuid,
                title = it.title,
                description = it.description,
                image_url = it.imageUrl,
                date_time = it.publishedAt,
                source = it.source,
                article_url = it.url,
                isBookmarked = false
            )
        }
    }

}
