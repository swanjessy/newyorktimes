package com.example.nytimes

import com.example.nytimes.data.model.moviesreview.Link
import com.example.nytimes.data.model.moviesreview.MovieReview
import com.example.nytimes.data.model.moviesreview.Multimedia
import com.example.nytimes.data.model.moviesreview.Result

/**
 * Data Set for Unit Testing
 */
object TestDataSet {

    fun getMovieReviewExpectedResult(): MovieReview {
        val result = mutableListOf<Result>()
        result.add(
            Result(
                display_title = "Three Months",

                mpaa_rating = "R",
                critics_pick = 1,
                byline = "Teo Bugbee",
                headline = "Three Months’ Review: A Refreshing Film Treatment of H.I.V.",
                summary_short = "This sensitive comedy by Jared Frieder sketches a relationship that blossoms in the shadow of not knowing.",
                publication_date = "2022-02-24",
                opening_date = "2022-02-23",
                date_updated = "2022-02-24 05:46:03",
                link = Link(
                    type = "Article",
                    url = "https://www.nytimes.com/2022/02/24/movies/three-months-movie-review.html",
                    suggested_link_text = "Read the New York Times Review of Three Months"
                ),
                multimedia = Multimedia(
                    type = "mediumThreeByTwo210",
                    src = "https://static01.nyt.com/images/2022/02/25/arts/24three-months-pix1/merlin_202495353_36452e30-7e55-4a98-a87c-2155b6c5e279-mediumThreeByTwo440.jpg",
                    height = 140,
                    width = 140
                )
            )
        )
        return MovieReview(
            status = "OK",
            copyright = "Copyright (c) 2022 The New York Times Company. All Rights Reserved.",
            has_more = true,
            num_results = 20,
            results = result
        )
    }
}
