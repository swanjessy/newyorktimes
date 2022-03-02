package com.example.nytimes.data.model.moviesreview

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieReview(
    val copyright: String,
    val has_more: Boolean,
    val num_results: Int,
    val results: List<Result>,
    val status: String
): Parcelable