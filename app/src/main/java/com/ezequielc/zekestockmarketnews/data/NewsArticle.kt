package com.ezequielc.zekestockmarketnews.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ezequielc.zekestockmarketnews.util.convertToTimestamp
import com.ezequielc.zekestockmarketnews.util.formatTimestamp
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "news_article")
data class NewsArticle(
    @PrimaryKey val uuid: String,
    val title: String,
    val description: String,
    val image_url: String,
    val date_time: String, // e.g. 2022-06-27T18:25:44.000000Z
    val timestamp: Long = convertToTimestamp(date_time),
    val pattern: String = "EEE, MMM dd, yyyy 'at' h:mm a",
    val source: String,
    val article_url: String,
    var isBookmarked: Boolean
) : Parcelable {
    val timestampFormatted: String
        get() = formatTimestamp(pattern, timestamp)
}

@Entity(tableName = "latest_news")
data class LatestNews(
    val article: NewsArticle,
    @PrimaryKey val articleId: String = article.uuid
)
