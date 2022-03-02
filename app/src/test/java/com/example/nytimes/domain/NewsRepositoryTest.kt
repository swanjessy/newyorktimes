package com.example.nytimes.domain

import com.example.nytimes.MockitoTest
import com.example.nytimes.data.model.moviesreview.Link
import com.example.nytimes.data.model.moviesreview.MovieReview
import com.example.nytimes.data.model.moviesreview.Multimedia
import com.example.nytimes.data.model.moviesreview.Result
import com.example.nytimes.domain.repository.NewsRepository
import com.example.nytimes.utils.Resource
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.IOException


@RunWith(JUnit4::class)
class NewsRepositoryTest : MockitoTest() {


    @Test
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `should get movie review repos on success`() = runTest {
        // GIVEN
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
        val response = Resource.Success(
            MovieReview(
                status = "OK",
                copyright = "Copyright (c) 2022 The New York Times Company. All Rights Reserved.",
                has_more = true,
                num_results = 20,
                results = result
            )
        )

        val newsRepository = mock<NewsRepository> {
            onBlocking {
                getMovieReview(
                    type = "picks",
                    offset = 10,
                    order = "by-opening-date"
                )
            } doReturn flowOf(response)
        }


        // WHEN
        val flow =
            newsRepository.getMovieReview(type = "picks", offset = 10, order = "by-opening-date")

        // THEN
        flow.collect { actualRepoList: Resource<MovieReview> ->
            actualRepoList.shouldBeEqualTo(response)
        }

    }

    @Test(expected = IOException::class)
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `should throw error for movie review repo if any exception is thrown`() = runTest {
        // GIVEN
        val newsRepository = mock<NewsRepository> {
            onBlocking {
                getMovieReview(
                    type = "picks",
                    offset = 10,
                    order = "by-opening-date"
                )
            } doAnswer {
                throw IOException()
            }
        }

        // WHEN
        val flow =
            newsRepository.getMovieReview(type = "picks", offset = 10, order = "by-opening-date")

        // THEN
        flow.collect { actualRepoList: Resource<MovieReview> ->
            actualRepoList.shouldBeNull()
        }
    }
}
