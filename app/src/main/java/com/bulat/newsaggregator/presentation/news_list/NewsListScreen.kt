package com.bulat.newsaggregator.presentation.news_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import coil.compose.rememberAsyncImagePainter
import com.bulat.newsaggregator.R
import com.bulat.newsaggregator.domain.model.NewsItem
import com.bulat.newsaggregator.presentation.NewsSortOrder
import com.bulat.newsaggregator.presentation.NewsUiState

@Composable
fun NewsListScreen(
    modifier: Modifier = Modifier,
    uiState: NewsUiState,
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
                TagCloud(
                    tags = tags,
                    selectedTag = selectedTag,
                    onTagSelected = onTagSelected
                )
        }

        Box(Modifier.fillMaxSize().weight(1f)) {
            when {

                uiState.isLoading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }

                uiState.error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.error, uiState.error),
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onRefresh) { Text(stringResource(R.string.retry)) }
                    }
                }

                uiState.news.isEmpty() -> {
                    Text(stringResource(R.string.no_news), modifier = Modifier.align(Alignment.Center))
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.news) {
                            NewsListItem(it, onClick = { onNewsClick(it) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TagCloud(tags: List<String>, selectedTag: String?, onTagSelected: (String?) -> Unit) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            AssistChip(
                onClick = { onTagSelected(null) },
                label = { Text(stringResource(R.string.all)) },
                colors = if (selectedTag == null)
                    AssistChipDefaults.assistChipColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                else AssistChipDefaults.assistChipColors()
            )
        }
        items(tags) { tag ->
            AssistChip(
                onClick = { onTagSelected(tag) },
                label = { Text(tag) },
                colors = if (selectedTag == tag)
                    AssistChipDefaults.assistChipColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                else AssistChipDefaults.assistChipColors()
            )
        }
    }
}

@Composable
fun NewsListItem(item: NewsItem, onClick: () -> Unit) {
    val formattedDescription = HtmlCompat.fromHtml(item.description, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            if (!item.imageUrl.isNullOrEmpty()) {
                Image(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    painter = rememberAsyncImagePainter(item.imageUrl),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                item.date?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                item.author?.let {
                    Text(
                        text = stringResource(R.string.author, it),
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (item.tags.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.tags) + " " + item.tags.joinToString(),
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = formattedDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun SortMenu(
    modifier: Modifier = Modifier,
    sortOrder: NewsSortOrder,
    onSortOrderChange: (NewsSortOrder) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier) {

        Button(
            onClick = { expanded = !expanded },
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
        ) {
            Text(
                if (sortOrder == NewsSortOrder.NEWEST) stringResource(R.string.newest_first)
                else stringResource(R.string.oldest_first)
            )
            Spacer(Modifier.width(3.dp))
            Icon(
                painter = painterResource(R.drawable.ic_round_sort_24),
                contentDescription = stringResource(R.string.sort_by)
            )
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.newest_first)) },
                onClick = {
                    onSortOrderChange(NewsSortOrder.NEWEST)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.oldest_first)) },
                onClick = {
                    onSortOrderChange(NewsSortOrder.OLDEST)
                    expanded = false
                }
            )
        }
    }
} 