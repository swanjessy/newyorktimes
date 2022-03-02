package com.example.nytimes.data.model.moviesreview

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Multimedia(
    val height: Int,
    val src: String,
    val type: String,
    val width: Int
): Parcelable