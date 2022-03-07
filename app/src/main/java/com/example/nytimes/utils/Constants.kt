package com.example.nytimes.utils

import com.example.nytimes.data.model.topstories.Category

/**
 * This class defines all the constants that are used in the application
 */
object Constants {

    //Database name
    const val DATABASE_NAME = "news_db"

    //Table name
    const val TABLE_ARTICLE = "articles"

    //Arguments Key
    const val ARGS_MOVIE = "movie"
    const val ARGS_ARTICLE = "article"

    //Tags
    const val TAG_HEADLINE = "Headlines"
    const val TAG_SECTION = "Section"

    //Movie Review constant params
    const val PARAM_PERIOD = 1
    const val PARAM_TYPE = "picks"
    const val PARAM_OFFSET = 10
    const val PARAM_ORDER = "by-opening-date"

    //Messages
    const val ERROR_MESSAGE_INVALID_KEY = "401 Unauthorized request"

    //Available list of category for top stories
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
