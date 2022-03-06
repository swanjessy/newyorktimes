package com.example.nytimes.data.db

import androidx.room.*
import com.example.nytimes.data.model.topstories.Article
import kotlinx.coroutines.flow.Flow

/**
 * DAO interface for ROOM DATABASE
 */
@Dao
interface ArticleDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article)

    @Query("SELECT * FROM articles")
    fun getAllArticles(): Flow<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}