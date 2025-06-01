package com.bulat.newsaggregator.domain.usecase

import com.bulat.newsaggregator.domain.repository.NewsRepository
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    operator fun invoke() = repository.getNews()
} 