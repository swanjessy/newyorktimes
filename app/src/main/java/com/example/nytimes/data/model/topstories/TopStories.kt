package com.example.nytimes.data.model.topstories

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TopStories(
    val copyright: String,
    val last_updated: String,
    val num_results: Int,
    val results: List<Result>? = null,
    val section: String,
    val status: String
) : Parcelable