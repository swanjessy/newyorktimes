package com.example.nytimes.data.repository

import com.example.nytimes.data.model.mostpopular.MostPopular
import com.example.nytimes.data.model.moviesreview.MovieReview
import com.example.nytimes.data.model.topstories.Article
import com.example.nytimes.data.model.topstories.TopStories
import com.example.nytimes.data.repository.dataSource.LocalDataSource
import com.example.nytimes.data.repository.dataSource.RemoteDataSource
import com.example.nytimes.domain.repository.NewsRepository
import com.example.nytimes.utils.Resource
import kotlinx.coroutines.flow.Flow

class NewsRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : NewsRepository {

    override suspend fun getMostPopularNews(period: Int): Flow<Resource<MostPopular>> {
        return remoteDataSource.getMostPopularNews(period)
    }

    override suspend fun getTopStories(section: String): Flow<Resource<TopStories>> {
        return remoteDataSource.getTopStories(section)
    }

    override suspend fun getMovieReview(
        type: String,
        offset: Int,
        order: String
    ): Flow<Resource<MovieReview>> {
        return remoteDataSource.getMovieReviews(type, offset, order)
    }

    override suspend fun saveArticle(article: Article) {
        localDataSource.saveArticleToDB(article)
    }

    override suspend fun deleteArticle(article: Article) {
        localDataSource.deleteArticlesFromDB(article)
    }

    override fun getSavedArticle(): Flow<List<Article>> {
        return localDataSource.getSavedArticles()
    }


}