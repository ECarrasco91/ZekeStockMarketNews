package com.ezequielc.zekestockmarketnews.di

import android.app.Application
import androidx.room.Room
import com.ezequielc.zekestockmarketnews.data.AppDatabase
import com.ezequielc.zekestockmarketnews.util.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application) = Room.databaseBuilder(
        application, AppDatabase::class.java, DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideNewsDao(db: AppDatabase) = db.newsArticleDao()
}