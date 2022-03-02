package com.example.nytimes.data.model.moviesreview

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Link(
    val suggested_link_text: String,
    val type: String,
    val url: String
) : Parcelable