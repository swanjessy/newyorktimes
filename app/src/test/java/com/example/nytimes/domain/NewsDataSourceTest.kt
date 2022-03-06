package com.example.nytimes.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.nytimes.TestCoroutineRule
import com.example.nytimes.TestDataSet.getMovieReviewExpectedResult
import com.example.nytimes.data.api.APIService
import com.example.nytimes.data.repository.dataSourceImpl.RemoteDataSourceImpl
import com.example.nytimes.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
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
    fun `should get movie review response success`() = runBlocking {
        val expectedResult = getMovieReviewExpectedResult()
        Mockito.doReturn(expectedResult)
            .`when`(apiService)
            .getMovieReview(type = "picks", offset = 10, order = "by-opening-date")

        val response =
            dataSource.getMovieReviews(type = "picks", offset = 10, order = "by-opening-date")
                .first()

        when (response) {
            is Resource.Success -> {
                val actualResult = response.data
                assertEquals(actualResult, expectedResult)
            }
        }
    }

    @Test
    fun `should get movie review response failure`() = runBlocking {
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

        assertThat(
            response,
            CoreMatchers.instanceOf(Resource.Error::class.java)
        )
    }
}