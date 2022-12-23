package com.ezequielc.zekestockmarketnews.ui.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ezequielc.zekestockmarketnews.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    val bookmarks = repository.getBookmarkedArticles().asLiveData()
}