package com.ezequielc.zekestockmarketnews.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ezequielc.zekestockmarketnews.data.NewsArticle
import com.ezequielc.zekestockmarketnews.repository.NewsRepository
import com.ezequielc.zekestockmarketnews.util.QUERY_DEFAULT
import com.ezequielc.zekestockmarketnews.util.QUERY_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: NewsRepository,
    state: SavedStateHandle
) : ViewModel() {

    init {
        if (!state.contains(QUERY_KEY)) {
            state[QUERY_KEY] = QUERY_DEFAULT
        }
    }

    val query = state.getLiveData<String>(QUERY_KEY)

    val results = state.getLiveData<String>(QUERY_KEY)
        .asFlow()
        .flatMapLatest { repository.getSearchResultPaged(it) }
        .cachedIn(viewModelScope)

    fun searchNews(query: String) {
        this.query.value = query
    }

    fun onBookmarkClick(newsArticle: NewsArticle) {
        val isBookmarked = newsArticle.isBookmarked
        val updatedArticle = newsArticle.copy(isBookmarked = !isBookmarked)
        viewModelScope.launch {
            repository.updateArticle(updatedArticle)
        }
    }
}