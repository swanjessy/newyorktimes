package com.example.nytimes.api

import com.example.nytimes.BuildConfig
import com.example.nytimes.data.api.APIService
import com.example.nytimes.utils.convertErrorBody
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

@RunWith(JUnit4::class)
class APIServiceTest : BaseApiTest() {

    private lateinit var service: APIService

    @Before
    @Throws(IOException::class)
    fun createService() {
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIService::class.java)
    }


    @Test
    @Throws(IOException::class, InterruptedException::class)
    fun getMostPopularNews_APISuccess() = runBlocking {
        enqueueResponse("mock_popular_news_success.json")
        val response = service.getMostPopularNews(1, BuildConfig.API_KEY)
        mockWebServer.takeRequest()

        MatcherAssert.assertThat(response, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(response.results?.size, CoreMatchers.`is`(20))
        MatcherAssert.assertThat(response.status, CoreMatchers.`is`("OK"))

        val articles = response.results
        MatcherAssert.assertThat(articles, CoreMatchers.notNullValue())

        val article1 = articles?.get(0)
        MatcherAssert.assertThat(article1, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(article1?.source, CoreMatchers.`is`("New York Times"))
        MatcherAssert.assertThat(article1?.type, CoreMatchers.`is`("Article"))
    }


    @Test
    @Throws(Throwable::class)
    fun getMostPopularNews_APIFailure() = runBlocking {
        lateinit var exceptionMessage: String

        try {
            service.getMostPopularNews(1, "")
            fail("Should throw an exception if apikey is not valid or empty")
        } catch (throwable: Throwable) {
            throwable.localizedMessage
            when (throwable) {
                is IOException -> {
                    exceptionMessage = throwable.message.toString()
                }
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = convertErrorBody(throwable)
                    exceptionMessage = errorResponse?.message.toString()
                }
                is KotlinNullPointerException -> {
                    exceptionMessage = "Null pointer Exception"
                }
                else -> {
                    exceptionMessage = "Some error has occurred"
                }
            }
        }

        MatcherAssert.assertThat(exceptionMessage, CoreMatchers.notNullValue())

    }
}
