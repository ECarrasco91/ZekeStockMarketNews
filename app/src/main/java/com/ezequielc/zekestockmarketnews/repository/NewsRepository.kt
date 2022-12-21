package com.ezequielc.zekestockmarketnews.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import com.ezequielc.zekestockmarketnews.data.AppDatabase
import com.ezequielc.zekestockmarketnews.data.LatestNews
import com.ezequielc.zekestockmarketnews.data.NewsArticle
import com.ezequielc.zekestockmarketnews.network.MarketNewsResponse
import com.ezequielc.zekestockmarketnews.network.StockDataService
import com.ezequielc.zekestockmarketnews.util.NETWORK_PAGE_SIZE
import com.ezequielc.zekestockmarketnews.util.NetworkBoundResource
import com.ezequielc.zekestockmarketnews.util.RateLimiter
import com.ezequielc.zekestockmarketnews.util.Resource
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val db: AppDatabase,
    private val service: StockDataService
) {
    private val newsArticleDao = db.newsArticleDao()
    private val rateLimiter = RateLimiter<String>(10, TimeUnit.MINUTES)

    suspend fun updateArticle(newsArticle: NewsArticle) = newsArticleDao.updateArticle(newsArticle)

    fun getSearchResultPaged(query: String) : Flow<PagingData<NewsArticle>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            remoteMediator = SearchRemoteMediator(query, db, service),
            pagingSourceFactory = { newsArticleDao.getSearchResultArticles(query) }
        ).flow
    }

    fun loadLatestMarketNews(key: String): Flow<Resource<List<NewsArticle>>> {
        return object : NetworkBoundResource<List<NewsArticle>, MarketNewsResponse>() {
            override fun processResponse(response: MarketNewsResponse) = response.asModel()

            override suspend fun saveCallResult(item: List<NewsArticle>) {
                val latestNews = item.map { newsArticle ->
                    LatestNews(newsArticle, newsArticle.uuid)
                }

                db.withTransaction {
                    newsArticleDao.insertArticles(item)
                    newsArticleDao.insertLatestArticles(latestNews)
                }
            }

            override fun shouldFetch(data: List<NewsArticle>?): Boolean {
                return rateLimiter.shouldFetch(key) || ((data == null) || data.isEmpty())
            }

            override fun loadFromDb() = newsArticleDao.getLatestNewsArticle()

            override suspend fun createCall() = service.getLatestMarketNews()

            override fun onFetchFailed() { rateLimiter.reset(key) }

        }.asFlow()
    }
}