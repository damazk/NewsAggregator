package com.bulat.newsaggregator.home.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bulat.newsaggregator.core.domain.model.NewsItem
import com.bulat.newsaggregator.core.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

enum class NewsSortOrder { NEWEST, OLDEST }

data class NewsUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val news: List<NewsItem> = emptyList(),
    val selectedTag: String? = null,
    val sortOrder: NewsSortOrder = NewsSortOrder.NEWEST,
    val searchQuery: String = ""
)

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    var uiState by mutableStateOf(NewsUiState())
        private set

    private var allNews: List<NewsItem> = emptyList()

    init {
        fetchNews()
    }

    fun fetchNews() {
        viewModelScope.launch(Dispatchers.IO) {
            uiState = uiState.copy(isLoading = true, error = null)
            try {
                newsRepository.getNews().collect { newsList ->
                    allNews = newsList
                    uiState = uiState.copy(
                        news = filterSortSearchNews(newsList, uiState.selectedTag, uiState.sortOrder, uiState.searchQuery),
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                uiState = NewsUiState(error = e.message)
            }
        }
    }

    fun selectTag(tag: String?) {
        uiState = uiState.copy(
            selectedTag = tag,
            news = filterSortSearchNews(allNews, tag, uiState.sortOrder, uiState.searchQuery)
        )
    }

    fun getAllTags() = allNews.flatMap { it.tags }.distinct().sorted()

    fun setSortOrder(order: NewsSortOrder) {
        uiState = uiState.copy(
            sortOrder = order,
            news = filterSortSearchNews(allNews, uiState.selectedTag, order, uiState.searchQuery)
        )
    }

    fun setSearchQuery(query: String) {
        uiState = uiState.copy(
            searchQuery = query,
            news = filterSortSearchNews(allNews, uiState.selectedTag, uiState.sortOrder, query)
        )
    }

    private fun filterSortSearchNews(news: List<NewsItem>, tag: String?, order: NewsSortOrder, searchQuery: String): List<NewsItem> {
        val filtered = if (tag.isNullOrEmpty()) news else news.filter { it.tags.contains(tag) }
        val searched = if (searchQuery.isBlank()) filtered else filtered.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
            it.description.contains(searchQuery, ignoreCase = true) ||
            it.tags.any { it.contains(searchQuery, ignoreCase = true) }
        }
        return searched.sortedWith(compareBy<NewsItem> {
            it.date?.let { dateStr ->
                try {
                    OffsetDateTime.parse(dateStr, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toEpochSecond()
                } catch (e: Exception) {
                    0L
                }
            } ?: 0L
        }.let { if (order == NewsSortOrder.NEWEST) it.reversed() else it })
    }
} 