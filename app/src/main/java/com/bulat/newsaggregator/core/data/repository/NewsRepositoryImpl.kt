package com.bulat.newsaggregator.core.data.repository

import com.bulat.newsaggregator.core.data.local.NewsDao
import com.bulat.newsaggregator.core.data.local.NewsEntity
import com.bulat.newsaggregator.core.data.remote.NewsApi
import com.bulat.newsaggregator.core.domain.model.NewsItem
import com.bulat.newsaggregator.core.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApi,
    private val dao: NewsDao
) : NewsRepository {
    override fun getNews(): Flow<List<NewsItem>> = flow {
        try {
            val response = api.getNews()
            val newsList = response.response.results.map { article ->
                NewsItem(
                    title = article.webTitle,
                    description = article.fields?.trailText ?: "",
                    imageUrl = article.fields?.thumbnail,
                    link = article.webUrl,
                    date = article.webPublicationDate,
                    author = article.byline,
                    tags = article.tags?.map { it.webTitle } ?: emptyList()
                )
            }
            dao.clearAll()
            dao.insertAll(newsList.map { it.toEntity() })
            emit(newsList)
        } catch (e: Exception) {
            val cached = dao.getAllNews().map { it.toNewsItem() }
            emit(cached)
        }
    }
}

private fun NewsItem.toEntity() = NewsEntity(
    link = link,
    title = title,
    description = description,
    imageUrl = imageUrl,
    date = date,
    author = author,
    tags = tags.joinToString(",")
)

private fun NewsEntity.toNewsItem() = NewsItem(
    title = title,
    description = description,
    imageUrl = imageUrl,
    link = link,
    date = date,
    author = author,
    tags = if (tags.isNotEmpty()) tags.split(",") else emptyList()
) 