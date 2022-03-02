package com.example.nytimes.data.api

import com.example.nytimes.BuildConfig
import com.example.nytimes.data.model.mostpopular.MostPopular
import com.example.nytimes.data.model.moviesreview.MovieReview
import com.example.nytimes.data.model.topstories.TopStories
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/***
 * This interface contains all service call methods for the app
 */
interface APIService {

    @GET("mostpopular/v2/viewed/{period}.json")
    suspend fun getMostPopularNews(
        @Path("period") period: Int,
        @Query("api-key") apiKey: String = BuildConfig.API_KEY
    ): MostPopular


    @GET("topstories/v2/{section}.json")
    suspend fun getTopStories(
        @Path("section") section: String,
        @Query("api-key")
        apiKey: String = BuildConfig.API_KEY
    ): TopStories


    @GET("movies/v2/reviews/{type}.json")
    suspend fun getMovieReview(
        @Path("type") type: String,
        @Query("offset")
        offset: Int,
        @Query("order")
        order: String,
        @Query("api-key")
        apiKey: String = BuildConfig.API_KEY
    ): MovieReview
}