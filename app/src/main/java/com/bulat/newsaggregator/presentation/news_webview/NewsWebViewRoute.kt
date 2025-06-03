package com.bulat.newsaggregator.presentation.news_webview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bulat.newsaggregator.R
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

const val NEWS_WEBVIEW_ROUTE = "news_webview"
const val URL_ARG = "url_arg"

fun NavGraphBuilder.newsWebView(navigateUp: () -> Unit) = composable(
    route = "$NEWS_WEBVIEW_ROUTE/{$URL_ARG}",
    arguments = listOf(navArgument(URL_ARG) { type = NavType.StringType })
) { backStackEntry ->
    val url = backStackEntry.arguments?.getString(URL_ARG)?.let {
        URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
    } ?: ""
    NewsWebViewRoute(
        navigateUp = navigateUp,
        url = url
    )
}

fun NavController.navigateToNewsWebView(url: String) =
    navigate(route = "$NEWS_WEBVIEW_ROUTE/$url") {
        launchSingleTop = true
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsWebViewRoute(
    navigateUp: () -> Unit,
    url: String,
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(
                    text = stringResource(R.string.news_aggregator),
                    style = MaterialTheme.typography.titleLarge
                ) },
                navigationIcon = {
                    IconButton(navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = Icons.AutoMirrored.Rounded.ArrowBack.name
                        )
                    }
                }
            )
        }
    ) { paddings ->
        NewsWebViewScreen(Modifier.padding(paddings), url)
    }

}