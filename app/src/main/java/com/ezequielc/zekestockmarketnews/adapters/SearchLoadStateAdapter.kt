package com.ezequielc.zekestockmarketnews.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezequielc.zekestockmarketnews.R
import com.ezequielc.zekestockmarketnews.databinding.SearchLoadingStateBinding

class SearchLoadStateAdapter(
    private val adapter: SearchPagingAdapter
) : LoadStateAdapter<SearchLoadStateAdapter.SearchLoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState,
    ) = SearchLoadStateViewHolder(parent) { adapter.retry() }

    override fun onBindViewHolder(
        holder: SearchLoadStateViewHolder,
        loadState: LoadState
    ) = holder.bind(loadState)

    class SearchLoadStateViewHolder(
        parent: ViewGroup,
        private val retryCallback: () -> Unit
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.search_loading_state, parent, false)
    ) {
        private val binding = SearchLoadingStateBinding.bind(itemView)
        private val progressBar = binding.progressBar
        private val errorMessage = binding.errorMessage
        private val retryButton = binding.retryButton
            .also {
                it.setOnClickListener { retryCallback() }
            }

        fun bind(loadState: LoadState) {
            progressBar.isVisible = loadState is LoadState.Loading
            retryButton.isVisible = loadState is LoadState.Error
            errorMessage.isVisible = !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()
            errorMessage.text = (loadState as? LoadState.Error)?.error?.message
        }
    }
}