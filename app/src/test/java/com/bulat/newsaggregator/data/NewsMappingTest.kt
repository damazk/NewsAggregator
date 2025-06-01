package com.bulat.newsaggregator.data

import com.bulat.newsaggregator.data.local.NewsEntity
import com.bulat.newsaggregator.domain.model.NewsItem
import org.junit.Assert.assertEquals
import org.junit.Test

class NewsMappingTest {
    @Test
    fun `NewsItem toEntity and back should be equal`() {
        val item = NewsItem(
            title = "Title",
            description = "Desc",
            imageUrl = "url",
            link = "link",
            date = "2024-01-01",
            author = "Author",
            tags = listOf("tag1", "tag2")
        )
        val entity = item.toEntity()
        val restored = entity.toNewsItem()
        assertEquals(item, restored)
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
} 