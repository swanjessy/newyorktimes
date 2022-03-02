package com.example.nytimes.presentation.di

import android.app.Application
import androidx.room.Room
import com.example.nytimes.data.db.ArticleDAO
import com.example.nytimes.data.db.NYDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Singleton
    @Provides
    fun provideNewsDatabase(app: Application): NYDatabase {
        return Room.databaseBuilder(app, NYDatabase::class.java, "news_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideNewsDao(articleDatabase: NYDatabase): ArticleDAO {
        return articleDatabase.getArticleDAO()
    }
}