package com.example.nytimes.presentation.di

import com.example.nytimes.presentation.ui.features.headlines.TopStoriesAdapter
import com.example.nytimes.presentation.ui.features.movies.MoviesReviewAdapter
import com.example.nytimes.presentation.ui.features.sections.CategoryAdapter
import com.example.nytimes.presentation.ui.features.sections.SectionAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AdapterModule {

    @Singleton
    @Provides
    fun provideTopStoriesAdapter(): TopStoriesAdapter {
        return TopStoriesAdapter()
    }

    @Singleton
    @Provides
    fun provideMoviesAdapter(): MoviesReviewAdapter {
        return MoviesReviewAdapter()
    }

    @Singleton
    @Provides
    fun provideCategoryAdapter(): CategoryAdapter {
        return CategoryAdapter()
    }

    @Singleton
    @Provides
    fun provideSectionsAdapter(): SectionAdapter {
        return SectionAdapter()
    }

}