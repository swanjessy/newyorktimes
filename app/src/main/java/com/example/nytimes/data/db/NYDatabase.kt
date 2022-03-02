package com.example.nytimes.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nytimes.data.model.topstories.Article

/**
 * Database class which extends ROOM DATABASE
 */
@Database(
    entities = [Article::class],
    version = 1,
    exportSchema = false
)
abstract class NYDatabase : RoomDatabase() {
    abstract fun getArticleDAO(): ArticleDAO
}

