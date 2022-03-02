package com.example.nytimes.data.model.topstories

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "articles"
)
@Parcelize
data class Article(
    @PrimaryKey(autoGenerate = false)
    val title: String,
    val description: String,
    val imageUrl: String,
    val url: String,
    val updated_date: String
) : Parcelable