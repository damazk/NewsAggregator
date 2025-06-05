package com.bulat.newsaggregator.core.domain.model

data class NewsItem(
    val title: String,
    val description: String,
    val imageUrl: String?,
    val link: String,
    val date: String?,
    val author: String?,
    val tags: List<String>
)