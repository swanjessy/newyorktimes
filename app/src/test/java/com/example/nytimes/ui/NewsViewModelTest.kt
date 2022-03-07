package com.example.nytimes.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.nytimes.TestCoroutineRule
import com.example.nytimes.TestDataSet.getMovieReviewExpectedResult
import com.example.nytimes.domain.usecase.*
import com.example.nytimes.presentation.ui.viewmodel.NewsViewModel
import com.example.nytimes.utils.Constants.ERROR_MESSAGE_INVALID_KEY
import com.example.nytimes.utils.Constants.PARAM_OFFSET
import com.example.nytimes.utils.Constants.PARAM_ORDER
import com.example.nytimes.utils.Constants.PARAM_TYPE
import com.example.nytimes.utils.NetworkManager
import com.example.nytimes.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
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
    fun `should return live data success when given response is success`() =
        testCoroutineRule.runBlockingTest {

            val movieReview = getMovieReviewExpectedResult()
            val expectedResponse = Resource.Success(movieReview)

            val flow = flow {
                emit(expectedResponse)
            }

            Mockito.doReturn(flow)
                .`when`(getMoviesUseCase)
                .invoke(type = PARAM_TYPE, offset = PARAM_OFFSET, order = PARAM_ORDER)

            viewModel.getMovieReview(type = PARAM_TYPE, offset = PARAM_OFFSET, order = PARAM_ORDER)

            Mockito.verify(getMoviesUseCase)
                .invoke(type = PARAM_TYPE, offset = PARAM_OFFSET, order = PARAM_ORDER)

            assertEquals(viewModel.movieReview.value, expectedResponse)
        }

    @Test
    fun `should return live data error when given response is failure`() =
        testCoroutineRule.runBlockingTest {

            val expectedResponse = Resource.Error(ERROR_MESSAGE_INVALID_KEY, null)

            val flow = flow {
                emit(expectedResponse)
            }

            Mockito.doReturn(flow)
                .`when`(getMoviesUseCase)
                .invoke(type = PARAM_TYPE, offset = PARAM_OFFSET, order = PARAM_ORDER)

            viewModel.getMovieReview(type = PARAM_TYPE, offset = PARAM_OFFSET, order = PARAM_ORDER)

            Mockito.verify(getMoviesUseCase)
                .invoke(type = PARAM_TYPE, offset = PARAM_OFFSET, order = PARAM_ORDER)

            assertEquals(viewModel.movieReview.value, expectedResponse)
        }
}
