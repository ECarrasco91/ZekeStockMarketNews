package com.ezequielc.zekestockmarketnews.ui.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezequielc.zekestockmarketnews.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClearBookmarksDialogViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    fun onConfirmClear() = viewModelScope.launch {
        repository.clearBookmarks()
    }
}