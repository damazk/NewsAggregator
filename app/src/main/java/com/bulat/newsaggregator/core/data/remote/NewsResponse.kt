package com.bulat.newsaggregator.core.data.remote

data class NetworkResponse<T>(
    val response: T
)

data class NewsResponse(
    val status: String,
    val results: List<NewsArticle>
)

data class NewsResults(
    val articles: List<NewsArticle>
)

data class NewsArticle(
    val webTitle: String,
    val webUrl: String,
    val webPublicationDate: String?,
    val fields: NewsFields?,
    val tags: List<NewsTag>?,
    val byline: String?
)

data class NewsFields(
    val thumbnail: String?,
    val trailText: String?
)

data class NewsTag(
    val webTitle: String
) 