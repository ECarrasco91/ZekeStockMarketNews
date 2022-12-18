package com.ezequielc.zekestockmarketnews.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ezequielc.zekestockmarketnews.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    fun loadLatestMarketNews(key: String) = repository.loadLatestMarketNews(key).asLiveData()
}