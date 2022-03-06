package com.example.nytimes.data.repository.dataSource

import com.example.nytimes.data.model.topstories.Article
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun saveArticleToDB(article: Article)

    fun getSavedArticles(): Flow<List<Article>>

    suspend fun deleteArticlesFromDB(article: Article)
}