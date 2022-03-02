package com.example.nytimes.presentation.di

import com.example.nytimes.data.repository.NewsRepositoryImpl
import com.example.nytimes.data.repository.dataSource.LocalDataSource
import com.example.nytimes.data.repository.dataSource.RemoteDataSource
import com.example.nytimes.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideNewsRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
    ): NewsRepository {
        return NewsRepositoryImpl(remoteDataSource, localDataSource)
    }

}














