package com.bulat.newsaggregator.domain.repository

import com.bulat.newsaggregator.domain.model.NewsItem
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNews(): Flow<List<NewsItem>>
} 