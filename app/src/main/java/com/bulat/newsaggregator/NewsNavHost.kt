package com.bulat.newsaggregator

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.bulat.newsaggregator.home.presentation.news_list.NEWS_LIST_ROUTE
import com.bulat.newsaggregator.home.presentation.news_list.newsList
import com.bulat.newsaggregator.home.presentation.news_webview.navigateToNewsWebView
import com.bulat.newsaggregator.home.presentation.news_webview.newsWebView

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewsNavHost(
    modifier: Modifier = Modifier
) {

    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = NEWS_LIST_ROUTE
    ) {

        newsList(navController::navigateToNewsWebView)

        newsWebView(navController::navigateUp)
    }
}