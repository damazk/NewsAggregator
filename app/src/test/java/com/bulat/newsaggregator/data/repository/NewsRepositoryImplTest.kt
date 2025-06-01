package com.bulat.newsaggregator.data.repository

import com.bulat.newsaggregator.data.local.NewsDao
import com.bulat.newsaggregator.data.local.NewsEntity
import com.bulat.newsaggregator.data.remote.NewsApi
import com.bulat.newsaggregator.domain.repository.NewsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NewsRepositoryImplTest {
    private val api: NewsApi = mockk()
    private val dao: NewsDao = mockk()
    private val repository: NewsRepository = NewsRepositoryImpl(api, dao)

    @Test
    fun `when api fails, returns cached news from db`() = runTest {
        coEvery { api.getNews(any(), any(), any()) } throws RuntimeException("fail")
        val cached = listOf(
            NewsEntity(
                link = "link",
                title = "title",
                description = "desc",
                imageUrl = null,
                date = "2024-01-01",
                author = "author",
                tags = "tag1,tag2"
            )
        )
        coEvery { dao.getAllNews() } returns cached
        val result = repository.getNews().first()
        assertEquals(1, result.size)
        assertEquals("title", result[0].title)
        assertEquals(listOf("tag1", "tag2"), result[0].tags)
    }
} 