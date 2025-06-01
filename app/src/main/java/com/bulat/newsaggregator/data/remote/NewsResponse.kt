package com.bulat.newsaggregator.data.remote

data class NewsResponse(
    val results: NewsResults
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