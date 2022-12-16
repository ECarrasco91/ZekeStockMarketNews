package com.ezequielc.zekestockmarketnews.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsArticleDao {

    @Query("SELECT * FROM latest_news " +
            "INNER JOIN news_article ON articleId = uuid " +
            "ORDER BY timestamp DESC")
    fun getLatestNewsArticle(): Flow<List<NewsArticle>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(newsArticles: List<NewsArticle>)

    @Update
    suspend fun updateArticle(newsArticle: NewsArticle)

}