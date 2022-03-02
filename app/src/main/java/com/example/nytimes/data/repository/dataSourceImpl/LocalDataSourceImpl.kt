package com.example.nytimes.data.repository.dataSourceImpl

import com.example.nytimes.data.db.ArticleDAO
import com.example.nytimes.data.model.topstories.Article
import com.example.nytimes.data.repository.dataSource.LocalDataSource
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl(
    private val articleDAO: ArticleDAO
) : LocalDataSource {

    override suspend fun saveArticleToDB(article: Article) {
        articleDAO.insert(article)
    }

    override fun getSavedArticles(): Flow<List<Article>> {
        return articleDAO.getAllArticles()
    }

    override suspend fun deleteArticlesFromDB(article: Article) {
        articleDAO.deleteArticle(article)
    }

}