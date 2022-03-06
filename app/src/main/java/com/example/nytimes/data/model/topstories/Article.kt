package com.example.nytimes.data.model.topstories

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.nytimes.utils.Constants.TABLE_ARTICLE
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = TABLE_ARTICLE
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