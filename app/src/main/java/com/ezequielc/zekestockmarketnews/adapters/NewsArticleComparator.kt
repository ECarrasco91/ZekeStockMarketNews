package com.ezequielc.zekestockmarketnews.adapters

import androidx.recyclerview.widget.DiffUtil
import com.ezequielc.zekestockmarketnews.data.NewsArticle

class NewsArticleComparator : DiffUtil.ItemCallback<NewsArticle>() {
    override fun areItemsTheSame(oldItem: NewsArticle, newItem: NewsArticle) =
        oldItem.uuid == newItem.uuid

    override fun areContentsTheSame(oldItem: NewsArticle, newItem: NewsArticle) =
        oldItem == newItem
}