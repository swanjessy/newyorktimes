package com.example.nytimes.data.model.mostpopular

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MostPopular(
    val copyright: String,
    val num_results: Int,
    val results: List<Result>? = null,
    val status: String
) : Parcelable