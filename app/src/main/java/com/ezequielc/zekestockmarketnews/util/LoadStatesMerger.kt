package com.ezequielc.zekestockmarketnews.util

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.scan

fun Flow<CombinedLoadStates>.asMergedLoadStates(): Flow<LoadStates> {
    val syncRemoteState = LoadStatesMerger()
    return scan(syncRemoteState.toLoadStates()) { _, combineLoadStates ->
        syncRemoteState.updateFromCombinedLoadStates(combineLoadStates)
        syncRemoteState.toLoadStates()
    }
}

private class LoadStatesMerger {
    var refresh: LoadState = LoadState.NotLoading(endOfPaginationReached = false)
        private set
    var prepend: LoadState = LoadState.NotLoading(endOfPaginationReached = false)
        private set
    var append: LoadState = LoadState.NotLoading(endOfPaginationReached = false)
        private set
    var refreshState: MergedState = MergedState.NOT_LOADING
        private set
    var prependState: MergedState = MergedState.NOT_LOADING
        private set
    var appendState: MergedState = MergedState.NOT_LOADING
        private set

    fun toLoadStates() = LoadStates(
        refresh = refresh,
        prepend = prepend,
        append = append
    )

    fun updateFromCombinedLoadStates(combinedLoadStates: CombinedLoadStates) {
        computeNextLoadStateAndMergedState(
            sourceRefreshState = combinedLoadStates.source.refresh,
            sourceState = combinedLoadStates.source.refresh,
            remoteState = combinedLoadStates.mediator?.refresh,
            currentMergedState = refreshState
        ).also {
            refresh = it.first
            refreshState = it.second
        }

        computeNextLoadStateAndMergedState(
            sourceRefreshState = combinedLoadStates.source.refresh,
            sourceState = combinedLoadStates.source.prepend,
            remoteState = combinedLoadStates.mediator?.prepend,
            currentMergedState = prependState
        ).also {
            prepend = it.first
            prependState = it.second
        }

        computeNextLoadStateAndMergedState(
            sourceRefreshState = combinedLoadStates.source.refresh,
            sourceState = combinedLoadStates.source.append,
            remoteState = combinedLoadStates.mediator?.append,
            currentMergedState = appendState
        ).also {
            append = it.first
            appendState = it.second
        }
    }

    private fun computeNextLoadStateAndMergedState(
        sourceRefreshState: LoadState,
        sourceState: LoadState,
        remoteState: LoadState?,
        currentMergedState: MergedState
    ): Pair<LoadState, MergedState> {
        if (remoteState == null) return sourceState to MergedState.NOT_LOADING

        return when (currentMergedState) {
            MergedState.NOT_LOADING -> when (remoteState) {
                is LoadState.Loading -> LoadState.Loading to MergedState.REMOTE_STARTED
                is PagingSource.LoadResult.Error<*, *> -> remoteState to MergedState.REMOTE_ERROR
                else -> LoadState.NotLoading(remoteState.endOfPaginationReached) to MergedState.NOT_LOADING
            }
            MergedState.REMOTE_STARTED -> when {
                remoteState is PagingSource.LoadResult.Error<*, *> -> remoteState to MergedState.REMOTE_ERROR
                sourceRefreshState is LoadState.Loading -> LoadState.Loading to MergedState.SOURCE_LOADING
                else -> LoadState.Loading to MergedState.REMOTE_STARTED
            }
            MergedState.REMOTE_ERROR -> when (remoteState) {
                is PagingSource.LoadResult.Error<*, *> -> remoteState to MergedState.REMOTE_ERROR
                else -> LoadState.Loading to MergedState.REMOTE_STARTED
            }
            MergedState.SOURCE_LOADING -> when {
                sourceRefreshState is PagingSource.LoadResult.Error<*, *> -> sourceRefreshState to MergedState.SOURCE_ERROR
                remoteState is PagingSource.LoadResult.Error<*, *> -> LoadState.Loading to MergedState.REMOTE_ERROR
                sourceRefreshState is LoadState.NotLoading -> {
                    LoadState.NotLoading(remoteState.endOfPaginationReached) to MergedState.NOT_LOADING
                }
                else -> LoadState.Loading to MergedState.SOURCE_LOADING
            }
            MergedState.SOURCE_ERROR -> when (sourceRefreshState) {
                is PagingSource.LoadResult.Error<*, *> -> sourceRefreshState to MergedState.SOURCE_ERROR
                else -> sourceRefreshState to MergedState.SOURCE_LOADING
            }
        }
    }
}

private enum class MergedState {
    NOT_LOADING,
    REMOTE_STARTED,
    REMOTE_ERROR,
    SOURCE_LOADING,
    SOURCE_ERROR
}