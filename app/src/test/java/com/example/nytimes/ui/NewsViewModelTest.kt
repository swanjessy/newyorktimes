package com.example.nytimes.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.nytimes.TestCoroutineRule
import com.example.nytimes.data.model.moviesreview.Link
import com.example.nytimes.data.model.moviesreview.MovieReview
import com.example.nytimes.data.model.moviesreview.Multimedia
import com.example.nytimes.data.model.moviesreview.Result
import com.example.nytimes.domain.usecase.*
import com.example.nytimes.presentation.viewmodel.NewsViewModel
import com.example.nytimes.utils.NetworkManager
import com.example.nytimes.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class NewsViewModelTest {
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var viewModel: NewsViewModel

    @Mock
    private lateinit var getMoviesUseCase: GetMoviesUseCase

    @Mock
    private lateinit var getMostPopularUseCase: GetMostPopularUseCase

    @Mock
    private lateinit var getTopStoriesUseCase: GetTopStoriesUseCase

    @Mock
    private lateinit var saveArticleUseCase: SaveArticleUseCase

    @Mock
    private lateinit var getSavedArticleUseCase: GetSavedArticleUseCase

    @Mock
    private lateinit var deleteArticleUseCase: DeleteArticleUseCase

    @Mock
    private lateinit var networkManager: NetworkManager

    @Before
    fun setup() {
        viewModel = NewsViewModel(
            getMostPopularUseCase,
            getTopStoriesUseCase,
            getMoviesUseCase,
            getSavedArticleUseCase,
            saveArticleUseCase,
            deleteArticleUseCase,
            networkManager
        )
    }


    @Test
    fun givenResponseSuccess_getAllMovies_shouldReturnSuccess() =
        testCoroutineRule.runBlockingTest {

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

            val movieReview = MovieReview(
                status = "OK",
                copyright = "Copyright (c) 2022 The New York Times Company. All Rights Reserved.",
                has_more = true,
                num_results = 20,
                results = result
            )
            val expectedResponse = Resource.Success(movieReview)

            val flow = flow {
                emit(expectedResponse)
            }

            Mockito.doReturn(flow)
                .`when`(getMoviesUseCase)
                .invoke(type = "picks", offset = 10, order = "by-opening-date")


            viewModel.getMovieReview(type = "picks", offset = 10, order = "by-opening-date")


            Mockito.verify(getMoviesUseCase)
                .invoke(type = "picks", offset = 10, order = "by-opening-date")

            Assert.assertEquals(viewModel.movieReview.value, expectedResponse)

        }


    @Test
    fun givenResponseError_getMovieReview_shouldReturnError() = testCoroutineRule.runBlockingTest {

        val expectedResponse = Resource.Error("401 Unauthorized request", null)

        val flow = flow {
            emit(
                expectedResponse
            )
        }

        Mockito.doReturn(flow)
            .`when`(getMoviesUseCase)
            .invoke(type = "picks", offset = 10, order = "by-opening-date")

        viewModel.getMovieReview(type = "picks", offset = 10, order = "by-opening-date")

        Mockito.verify(getMoviesUseCase)
            .invoke(type = "picks", offset = 10, order = "by-opening-date")

        Assert.assertEquals(viewModel.movieReview.value, expectedResponse)

    }


}
