package com.example.nytimes.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.nytimes.TestCoroutineRule
import com.example.nytimes.data.api.APIService
import com.example.nytimes.data.model.moviesreview.Link
import com.example.nytimes.data.model.moviesreview.MovieReview
import com.example.nytimes.data.model.moviesreview.Multimedia
import com.example.nytimes.data.model.moviesreview.Result
import com.example.nytimes.data.repository.dataSourceImpl.RemoteDataSourceImpl
import com.example.nytimes.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class NewsDataSourceTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    lateinit var apiService: APIService

    private lateinit var dataSource: RemoteDataSourceImpl


    @Before
    fun setup() {
        dataSource = RemoteDataSourceImpl(apiService)
    }

    @Test
    fun getMovieReviews_response_success() = runBlocking {

        val result = mutableListOf<Result>()
        result.add(
            Result(
                display_title = "Three Months",
                mpaa_rating = "R",
                critics_pick = 1,
                byline = "Teo Bugbee",
                headline = "Three Months’ Review: A Refreshing Film Treatment of H.I.V.",
                summary_short = "This sensitive comedy by Jared Frieder sketches a relationship that blossoms in the shadow of not knowing.",
                publication_date = "2022-02-24",
                opening_date = "2022-02-23",
                date_updated = "2022-02-24 05:46:03",
                link = Link(
                    type = "Article",
                    url = "https://www.nytimes.com/2022/02/24/movies/three-months-movie-review.html",
                    suggested_link_text = "Read the New York Times Review of Three Months"
                ),
                multimedia = Multimedia(
                    type = "mediumThreeByTwo210",
                    src = "https://static01.nyt.com/images/2022/02/25/arts/24three-months-pix1/merlin_202495353_36452e30-7e55-4a98-a87c-2155b6c5e279-mediumThreeByTwo440.jpg",
                    height = 140,
                    width = 140
                )
            )
        )
        val expectedResult = MovieReview(
            status = "OK",
            copyright = "Copyright (c) 2022 The New York Times Company. All Rights Reserved.",
            has_more = true,
            num_results = 20,
            results = result
        )


        Mockito.doReturn(expectedResult)
            .`when`(apiService)
            .getMovieReview(type = "picks", offset = 10, order = "by-opening-date")

        val response =
            dataSource.getMovieReviews(type = "picks", offset = 10, order = "by-opening-date")
                .first()

        when (response) {
            is Resource.Success -> {
                val actualResult = response.data
                Assert.assertEquals(actualResult, expectedResult)
            }
        }

    }

    @Test
    fun getMovieReviews_response_failed() = runBlocking {

        given(
            apiService.getMovieReview(
                type = "picks",
                offset = 10,
                order = "by-opening-date"
            )
        ).willAnswer {
            throw IOException()
        }

        val response =
            dataSource.getMovieReviews(type = "picks", offset = 10, order = "by-opening-date")
                .first()

        MatcherAssert.assertThat(
            response,
            CoreMatchers.instanceOf(Resource.Error::class.java)
        )

    }


}