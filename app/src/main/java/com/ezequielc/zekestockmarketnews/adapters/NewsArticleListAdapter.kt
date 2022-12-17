package com.ezequielc.zekestockmarketnews.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.ezequielc.zekestockmarketnews.data.NewsArticle
import com.ezequielc.zekestockmarketnews.databinding.NewsListItemBinding

class NewsArticleListAdapter :
    ListAdapter<NewsArticle, NewsArticleViewHolder>(NewsArticleComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsArticleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = NewsListItemBinding.inflate(layoutInflater, parent, false)
        return NewsArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsArticleViewHolder, position: Int) {
        val newsArticle = getItem(position)
        if (newsArticle != null) {
            holder.bind(newsArticle)
        }
    }
}