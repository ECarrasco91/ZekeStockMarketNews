package com.ezequielc.zekestockmarketnews.interfaces

import com.ezequielc.zekestockmarketnews.data.NewsArticle

/**
 * Listener used to process when user clicks on item in RecyclerView
 */
interface OnNewsArticleClickListener {

    fun onBookmarkClick(newsArticle: NewsArticle)
}