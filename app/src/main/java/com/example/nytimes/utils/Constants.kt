package com.example.nytimes.utils

import com.example.nytimes.data.model.topstories.Category

object Constants {

    const val DATABASE_NAME = "news_db"
    const val TABLE_ARTICLE = "articles"
    const val ARGS_MOVIE = "movie"
    const val ARGS_ARTICLE = "article"

    val category = listOf(
        Category("arts"),
        Category("automobiles"),
        Category("books"),
        Category("fashion"),
        Category("food"),
        Category("health"),
        Category("sports"),
        Category("magazine"),
        Category("travel")
    )
}
