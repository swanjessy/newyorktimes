package com.example.nytimes.db

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.nytimes.data.db.NYDatabase
import com.example.nytimes.data.model.topstories.Article
import com.example.nytimes.utils.DaoTest
import com.example.nytimes.utils.assertItems
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewsArticlesDaoTest : DaoTest<NYDatabase>(NYDatabase::class.java) {

    @Test
    @Throws(InterruptedException::class)
    fun insertNewsArticleTest() = runBlocking {
        // GIVEN
        val article = Article("Title 1", "Descrp 1", "image 1", "url 1", "date 1")
        val insertItem = db.getArticleDAO().insert(article)

        // THEN
        assertThat(article, CoreMatchers.notNullValue())
        assertThat(insertItem, CoreMatchers.notNullValue())
    }

    @Test
    @Throws(InterruptedException::class)
    fun insertNewsArticlesAndReadTest() = runBlocking {
        // GIVEN
        val input1 = Article("Title 1", "Descrp 1", "image 1", "url 1", "date 1")
        val input2 = Article("Title 2", "Descrp 2", "image 2", "url 2", "date 2")
        val mockResponse = mutableListOf(input1, input2)

        db.getArticleDAO().insert(input1)
        db.getArticleDAO().insert(input2)

        val expectedResponse = db.getArticleDAO().getAllArticles()

        // THEN
        assertThat(mockResponse, CoreMatchers.notNullValue())
        assertThat(expectedResponse, CoreMatchers.notNullValue())
        expectedResponse.assertItems(mockResponse)
    }

    @Test
    @Throws(InterruptedException::class)
    fun deleteArticleTest() = runBlocking {
        // GIVEN
        val article = Article("Title 1", "Descrp 1", "image 1", "url 1", "date 1")
        db.getArticleDAO().insert(article)
        db.getArticleDAO().deleteArticle(article)

        lateinit var actualList: List<Article>
        // WHEN
        val flow = db.getArticleDAO().getAllArticles()
        flow.take(1).collect { actualRepoList: List<Article> ->
            actualList = actualRepoList
        }

        // THEN
        assertTrue(actualList.isEmpty())
    }
}