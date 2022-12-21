package com.ezequielc.zekestockmarketnews.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [NewsArticle::class, LatestNews::class, SearchResults::class, SearchQueryRemoteKey::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsArticleDao(): NewsArticleDao
    abstract fun searchQueryRemoteKeyDao(): SearchQueryRemoteKeyDao
}