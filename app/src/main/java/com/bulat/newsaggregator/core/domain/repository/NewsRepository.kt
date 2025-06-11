package com.bulat.newsaggregator.core.domain.repository

import com.bulat.newsaggregator.core.domain.model.NewsItem
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNews(): Flow<Result<List<NewsItem>>>
} 