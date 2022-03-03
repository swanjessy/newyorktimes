package com.example.nytimes.domain

import com.example.nytimes.MockitoTest
import com.example.nytimes.TestDataSet.getMovieReviewExpectedResult
import com.example.nytimes.data.model.moviesreview.MovieReview
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
        val expectedResult = getMovieReviewExpectedResult()
        val response = Resource.Success(expectedResult)

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
