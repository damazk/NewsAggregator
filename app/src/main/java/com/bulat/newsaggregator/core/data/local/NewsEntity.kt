package com.bulat.newsaggregator.core.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsEntity(
    @PrimaryKey val link: String,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val date: String?,
    val author: String?,
    val tags: String
)