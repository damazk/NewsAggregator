package com.bulat.newsaggregator.core.domain.usecase

import com.bulat.newsaggregator.core.domain.repository.NewsRepository
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    operator fun invoke() = repository.getNews()
} 