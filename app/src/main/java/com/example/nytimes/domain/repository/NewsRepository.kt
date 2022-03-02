package com.example.nytimes.domain.repository

import com.example.nytimes.data.model.mostpopular.MostPopular
import com.example.nytimes.data.model.moviesreview.MovieReview
import com.example.nytimes.data.model.topstories.Article
import com.example.nytimes.data.model.topstories.TopStories
import com.example.nytimes.utils.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun getMostPopularNews(period: Int): Flow<Resource<MostPopular>>

    suspend fun getTopStories(section: String): Flow<Resource<TopStories>>

    suspend fun getMovieReview(
        type: String,
        offset: Int,
        order: String
    ): Flow<Resource<MovieReview>>

    suspend fun saveArticle(article: Article)

    suspend fun deleteArticle(article: Article)

    fun getSavedArticle(): Flow<List<Article>>
}