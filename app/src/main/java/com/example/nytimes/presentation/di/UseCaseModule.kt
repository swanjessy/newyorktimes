package com.example.nytimes.presentation.di

import com.example.nytimes.domain.repository.NewsRepository
import com.example.nytimes.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Singleton
    @Provides
    fun provideGetMostPopularUseCase(
        newsRepository: NewsRepository
    ): GetMostPopularUseCase {
        return GetMostPopularUseCase(newsRepository)
    }

    @Singleton
    @Provides
    fun provideGetMoviesUseCase(
        newsRepository: NewsRepository
    ): GetMoviesUseCase {
        return GetMoviesUseCase(newsRepository)
    }

    @Singleton
    @Provides
    fun provideTopStoriesUseCase(
        newsRepository: NewsRepository
    ): GetTopStoriesUseCase {
        return GetTopStoriesUseCase(newsRepository)
    }

    @Singleton
    @Provides
    fun provideGetSavedArticleUseCase(
        newsRepository: NewsRepository
    ): GetSavedArticleUseCase {
        return GetSavedArticleUseCase(newsRepository)
    }

    @Singleton
    @Provides
    fun provideDeleteSavedArticleUseCase(
        newsRepository: NewsRepository
    ): DeleteArticleUseCase {
        return DeleteArticleUseCase(newsRepository)
    }


    @Singleton
    @Provides
    fun provideSaveArticleUseCase(
        newsRepository: NewsRepository
    ): SaveArticleUseCase {
        return SaveArticleUseCase(newsRepository)
    }

}


















