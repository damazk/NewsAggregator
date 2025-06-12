package com.bulat.newsaggregator.home.presentation.news_list

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bulat.newsaggregator.R
import com.bulat.newsaggregator.home.presentation.NewsViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

const val NEWS_LIST_ROUTE = "news_list"

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.newsList(navigateToNewsWebView: (String) -> Unit) = composable(NEWS_LIST_ROUTE) {
    NewsListRoute(navigateToNewsWebView)
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListRoute(
    navigateToNewsWebView: (String) -> Unit,
    viewModel: NewsViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState
    val tags = uiState.tags
    val selectedTag = uiState.selectedTag
    val sortOrder = uiState.sortOrder
    val searchQuery = uiState.searchQuery

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(
                    text = stringResource(R.string.news_aggregator),
                    style = MaterialTheme.typography.titleLarge
                ) },
                navigationIcon = {}
            )
        }
    ) { paddings ->
        NewsListScreen(
            modifier = Modifier.padding(paddings),
            uiState = uiState,
            onNewsClick = { newsItem ->
                val encodedUrl = URLEncoder.encode(newsItem.link, StandardCharsets.UTF_8.toString())
                navigateToNewsWebView(encodedUrl)
            },
            onRefresh = { viewModel.fetchNews() },
            tags = tags,
            selectedTag = selectedTag,
            onTagSelected = { viewModel.selectTag(it) },
            sortOrder = sortOrder,
            onSortOrderChange = { viewModel.setSortOrder(it) },
            searchQuery = searchQuery,
            onSearchQueryChange = { viewModel.setSearchQuery(it) }
        )
    }
}