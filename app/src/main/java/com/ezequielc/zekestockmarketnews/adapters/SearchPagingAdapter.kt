package com.ezequielc.zekestockmarketnews.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.ezequielc.zekestockmarketnews.data.NewsArticle
import com.ezequielc.zekestockmarketnews.databinding.NewsListItemBinding
import com.ezequielc.zekestockmarketnews.interfaces.OnNewsArticleClickListener

class SearchPagingAdapter(
    private val listener: OnNewsArticleClickListener
) : PagingDataAdapter<NewsArticle, NewsArticleViewHolder>(NewsArticleComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsArticleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = NewsListItemBinding.inflate(layoutInflater, parent, false)
        return NewsArticleViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: NewsArticleViewHolder, position: Int) {
        val newsArticle = getItem(position)
        if (newsArticle != null) {
            holder.bind(newsArticle)
        }
    }
}