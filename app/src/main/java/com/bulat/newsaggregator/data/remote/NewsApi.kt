package com.bulat.newsaggregator.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("search")
    suspend fun getNews(
        @Query("show-fields") showFields: String = "thumbnail,trailText,byline",
        @Query("show-tags") showTags: String = "keyword",
        @Query("order-by") orderBy: String = "newest"
    ): NewsResponse
}