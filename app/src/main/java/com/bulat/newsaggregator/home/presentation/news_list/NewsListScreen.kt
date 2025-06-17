package com.bulat.newsaggregator.home.presentation.news_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bulat.newsaggregator.R
import com.bulat.newsaggregator.core.composables.drop_down_menus.SortMenu
import com.bulat.newsaggregator.core.composables.items.NewsListItem
import com.bulat.newsaggregator.core.composables.items.TagItem
import com.bulat.newsaggregator.core.domain.model.NewsItem
import com.bulat.newsaggregator.home.presentation.NewsSortOrder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListScreen(
    modifier: Modifier = Modifier,
    news: List<NewsItem>,
    isLoading: Boolean,
    error: String?,
    onNewsClick: (NewsItem) -> Unit,
    onRefresh: () -> Unit,
    tags: List<String>,
    selectedTag: String?,
    onTagSelected: (String?) -> Unit,
    sortOrder: NewsSortOrder,
    onSortOrderChange: (NewsSortOrder) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    Column(
        modifier.fillMaxSize().padding(horizontal = 16.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text(stringResource(R.string.search)) },
            singleLine = true
        )

        Row(
            Modifier.fillMaxWidth().padding(vertical = 5.dp)
        ) {
            SortMenu(
                sortOrder = sortOrder,
                onSortOrderChange = onSortOrderChange
            )
            Spacer(Modifier.width(5.dp))
            if (tags.isNotEmpty())
                TagClouds(
                    tags = tags,
                    selectedTag = selectedTag,
                    onTagSelected = onTagSelected
                )
        }

        Box(Modifier.fillMaxSize().weight(1f)) {
            when {

                isLoading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }

                error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.error, error),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onRefresh) { Text(stringResource(R.string.retry)) }
                    }
                }

                news.isEmpty() -> {
                    Text(stringResource(R.string.no_news), modifier = Modifier.align(Alignment.Center))
                }

                else -> {
                    PullToRefreshBox(
                        modifier = Modifier.fillMaxSize(),
                        isRefreshing = isLoading,
                        onRefresh = onRefresh
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(news) {
                                NewsListItem(it, onClick = { onNewsClick(it) })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TagClouds(tags: List<String>, selectedTag: String?, onTagSelected: (String?) -> Unit) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            TagItem(
                onClick = { onTagSelected(null) },
                tagText = stringResource(R.string.all),
                isSelected = selectedTag == null,
            )
        }
        items(tags) { tag ->
            TagItem(
                onClick = { onTagSelected(tag) },
                tagText = tag,
                isSelected = selectedTag == tag
            )
        }
    }
}