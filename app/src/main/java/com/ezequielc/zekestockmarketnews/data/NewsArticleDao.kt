package com.ezequielc.zekestockmarketnews.data

import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsArticleDao {

    // QUERY FUNCTIONS

    @Query("SELECT * FROM latest_news " +
            "INNER JOIN news_article ON articleId = uuid " +
            "ORDER BY timestamp DESC")
    fun getLatestNewsArticle(): Flow<List<NewsArticle>>

    @Query("SELECT * FROM search_results " +
            "INNER JOIN news_article ON articleId = uuid " +
            "WHERE `query` = :query ORDER BY queryPosition")
    fun getSearchResultArticles(query: String): PagingSource<Int, NewsArticle>

    @Query("SELECT MAX(queryPosition) FROM search_results " +
            "WHERE `query` = :query ORDER BY queryPosition")
    fun getLastQueryPosition(query: String): Int?

    @Query("SELECT * FROM news_article WHERE isBookmarked = 1")
    fun getBookmarkedArticles(): Flow<List<NewsArticle>>

    // INSERT FUNCTIONS

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(newsArticles: List<NewsArticle>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLatestArticles(newsArticles: List<LatestNews>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchResults(results: List<SearchResults>)

    // UPDATE FUNCTIONS

    @Update
    suspend fun updateArticle(newsArticle: NewsArticle)

    @Query("UPDATE news_article SET isBookmarked = 0")
    suspend fun clearBookmarks()

    // DELETE FUNCTIONS

    @Query("DELETE FROM search_results WHERE `query` = :query")
    suspend fun deleteSearchResultsForQuery(query: String)
}