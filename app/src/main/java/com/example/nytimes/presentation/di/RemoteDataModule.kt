package com.example.nytimes.presentation.di

import com.example.nytimes.data.api.APIService
import com.example.nytimes.data.repository.dataSource.RemoteDataSource
import com.example.nytimes.data.repository.dataSourceImpl.RemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RemoteDataModule {

    @Singleton
    @Provides
    fun provideNewsRemoteDataSource(
        apiService: APIService
    ): RemoteDataSource {
        return RemoteDataSourceImpl(apiService)
    }

}












