package com.example.nytimes.data.model.mostpopular

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Result(
    val abstract: String,
    val adx_keywords: String,
    val asset_id: Long,
    val byline: String,
    val column: String?,
    val des_facet: List<String>,
    val eta_id: Int,
    val geo_facet: List<String>,
    val id: Long,
    val media: List<Media>,
    val nytdsection: String,
    val org_facet: List<String>,
    val per_facet: List<String>,
    val published_date: String,
    val section: String,
    val source: String,
    val subsection: String,
    val title: String,
    val type: String,
    val updated: String,
    val uri: String,
    val url: String
) : Parcelable