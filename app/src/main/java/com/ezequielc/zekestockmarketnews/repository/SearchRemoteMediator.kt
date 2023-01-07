package com.ezequielc.zekestockmarketnews.repository

import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ezequielc.zekestockmarketnews.data.AppDatabase
import com.ezequielc.zekestockmarketnews.data.NewsArticle
import com.ezequielc.zekestockmarketnews.data.SearchQueryRemoteKey
import com.ezequielc.zekestockmarketnews.data.SearchResults
import com.ezequielc.zekestockmarketnews.network.StockDataService
import com.ezequielc.zekestockmarketnews.util.STOCK_DATA_STARTING_PAGE_INDEX
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.IOException

class SearchRemoteMediator(
    private val query: String,
    private val db: AppDatabase,
    private val service: StockDataService
) : RemoteMediator<Int, NewsArticle>() {
    private val newsArticleDao = db.newsArticleDao()
    private val remoteKeyDao = db.searchQueryRemoteKeyDao()

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NewsArticle>,
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> STOCK_DATA_STARTING_PAGE_INDEX
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = db.withTransaction {
                        remoteKeyDao.getRemoteKey(query)
                    }

                    if (remoteKey.nextPageKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    remoteKey.nextPageKey
                }
            }

            val response = service.querySearchResults(search = query, page = page)
            val articles = response.asModel()

            val bookmarkedArticles = newsArticleDao.getBookmarkedArticles().first()

            val searchResultArticles = articles.map { searchResultArticle ->
                val isBookmarked = bookmarkedArticles.any { bookmarkedArticle ->
                    bookmarkedArticle.uuid == searchResultArticle.uuid
                }

                NewsArticle(
                    uuid = searchResultArticle.uuid,
                    title = searchResultArticle.title,
                    description = searchResultArticle.description,
                    image_url = searchResultArticle.image_url,
                    date_time = searchResultArticle.date_time,
                    source = searchResultArticle.source,
                    article_url = searchResultArticle.article_url,
                    isBookmarked = isBookmarked,
                    tickers = searchResultArticle.tickers
                )
            }

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.deleteSearchQuery(query)
                    newsArticleDao.deleteSearchResultsForQuery(query)
                }

                val lastQueryPosition = newsArticleDao.getLastQueryPosition(query) ?: 0
                var queryPosition = lastQueryPosition + 1

                val searchResults = searchResultArticles.map { article ->
                    SearchResults(query, queryPosition++, article, article.uuid)
                }

                val nextPageKey = page + 1

                newsArticleDao.insertArticles(searchResultArticles)
                newsArticleDao.insertSearchResults(searchResults)
                remoteKeyDao.insert(SearchQueryRemoteKey(query, nextPageKey))

                MediatorResult.Success(endOfPaginationReached = articles.isEmpty())
            }

        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }
}