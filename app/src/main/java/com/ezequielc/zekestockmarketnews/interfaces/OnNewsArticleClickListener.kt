package com.ezequielc.zekestockmarketnews.interfaces

import com.ezequielc.zekestockmarketnews.data.NewsArticle

/**
 * Listener used to process when user clicks on item in RecyclerView
 */
interface OnNewsArticleClickListener {

    fun onItemClick(newsArticle: NewsArticle)

    fun onBookmarkClick(newsArticle: NewsArticle)
}