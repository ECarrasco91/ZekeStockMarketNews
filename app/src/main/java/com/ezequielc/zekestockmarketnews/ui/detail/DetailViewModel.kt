package com.ezequielc.zekestockmarketnews.ui.detail

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezequielc.zekestockmarketnews.R
import com.ezequielc.zekestockmarketnews.data.NewsArticle
import com.ezequielc.zekestockmarketnews.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    fun onBookmarkClick(newsArticle: NewsArticle, imageview: ImageView) {
        val isBookmarked = newsArticle.isBookmarked
        val updatedArticle = newsArticle.copy(isBookmarked = !isBookmarked)
        viewModelScope.launch {
            repository.updateArticle(updatedArticle)
            updateBookmarkUI(updatedArticle.isBookmarked, imageview)
        }
    }

    private fun updateBookmarkUI(isBookmarked: Boolean, imageview: ImageView) {
        imageview.setImageResource(
            when (isBookmarked) {
                true -> R.drawable.ic_bookmark_filled_24dp
                false -> R.drawable.ic_bookmark_border_24dp
            }
        )
    }
}