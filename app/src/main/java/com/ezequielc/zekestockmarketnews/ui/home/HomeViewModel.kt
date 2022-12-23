package com.ezequielc.zekestockmarketnews.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ezequielc.zekestockmarketnews.data.NewsArticle
import com.ezequielc.zekestockmarketnews.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    fun loadLatestMarketNews(key: String) = repository.loadLatestMarketNews(key).asLiveData()

    fun onBookmarkClick(newsArticle: NewsArticle) {
        val isBookmarked = newsArticle.isBookmarked
        val updatedArticle = newsArticle.copy(isBookmarked = !isBookmarked)
        viewModelScope.launch {
            repository.updateArticle(updatedArticle)
        }
    }
}