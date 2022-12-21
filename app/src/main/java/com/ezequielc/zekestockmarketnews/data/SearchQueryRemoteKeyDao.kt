package com.ezequielc.zekestockmarketnews.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SearchQueryRemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(keys: SearchQueryRemoteKey)

    @Query("SELECT * FROM search_query_remote_keys WHERE searchQuery = :searchQuery")
    suspend fun getRemoteKey(searchQuery: String): SearchQueryRemoteKey

    @Query("DELETE FROM search_query_remote_keys WHERE searchQuery = :searchQuery")
    suspend fun deleteSearchQuery(searchQuery: String)
}