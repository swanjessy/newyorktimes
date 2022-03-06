package com.example.nytimes.data.repository.dataSourceImpl

import com.example.nytimes.data.api.APIService
import com.example.nytimes.data.model.mostpopular.MostPopular
import com.example.nytimes.data.model.moviesreview.MovieReview
import com.example.nytimes.data.model.topstories.TopStories
import com.example.nytimes.data.repository.dataSource.RemoteDataSource
import com.example.nytimes.utils.Resource
import com.example.nytimes.utils.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSourceImpl(
    private val apiService: APIService
) : RemoteDataSource {

    override suspend fun getMostPopularNews(period: Int): Flow<Resource<MostPopular>> {
        return flow {
            val response = safeApiCall(Dispatchers.IO) {
                apiService.getMostPopularNews(period)
            }
            emit(response)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getTopStories(section: String): Flow<Resource<TopStories>> {
        return flow {
            val response = safeApiCall(Dispatchers.IO) {
                apiService.getTopStories(section)
            }
            emit(response)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getMovieReviews(
        type: String,
        offset: Int,
        order: String
    ): Flow<Resource<MovieReview>> {
        return flow {
            val response = safeApiCall(Dispatchers.IO) {
                apiService.getMovieReview(type, offset, order)
            }
            emit(response)
        }.flowOn(Dispatchers.IO)
    }
}