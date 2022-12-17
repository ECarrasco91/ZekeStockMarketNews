package com.ezequielc.zekestockmarketnews.util

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.*

/**
 * A generic class that can provide a resource backed by both the database and network
 *
 * @param ResultType - represents the data type for Resource data
 * @param RequestType - represents the data type for API response
 */
abstract class NetworkBoundResource<ResultType, RequestType> {
    fun asFlow() = flow<Resource<ResultType>> {
        emit(Resource.Loading(null))

        val dbSource = loadFromDb()

        if (shouldFetch(dbSource.first())) {
            try {
                saveCallResult(processResponse(createCall()))
            } catch (exception: Exception) {
                onFetchFailed()
                emit(Resource.Error(error = exception.toString()))
                return@flow
            }

            emitAll(loadFromDb().map { Resource.Success(it) })
        }
    }

    protected open fun onFetchFailed() {}

    @WorkerThread
    protected abstract fun processResponse(response: RequestType): ResultType

    @WorkerThread
    protected abstract suspend fun saveCallResult(item: ResultType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): Flow<ResultType>

    @MainThread
    protected abstract suspend fun createCall(): RequestType
}