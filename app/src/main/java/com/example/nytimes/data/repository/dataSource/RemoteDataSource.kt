package com.example.nytimes.data.repository.dataSource

import com.example.nytimes.data.model.mostpopular.MostPopular
import com.example.nytimes.data.model.moviesreview.MovieReview
import com.example.nytimes.data.model.topstories.TopStories
import com.example.nytimes.utils.Resource
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

    suspend fun getMostPopularNews(period: Int): Flow<Resource<MostPopular>>

    suspend fun getTopStories(section: String): Flow<Resource<TopStories>>

    suspend fun getMovieReviews(
        type: String,
        offset: Int,
        order: String
    ): Flow<Resource<MovieReview>>
}