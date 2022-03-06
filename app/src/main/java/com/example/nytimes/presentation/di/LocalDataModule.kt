package com.example.nytimes.presentation.di

import com.example.nytimes.data.db.ArticleDAO
import com.example.nytimes.data.repository.dataSource.LocalDataSource
import com.example.nytimes.data.repository.dataSourceImpl.LocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataModule {
    @Singleton
    @Provides
    fun provideLocalDataSource(articleDAO: ArticleDAO): LocalDataSource {
        return LocalDataSourceImpl(articleDAO)
    }
}













