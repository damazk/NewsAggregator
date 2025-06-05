package com.bulat.newsaggregator.core.composables.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import coil.compose.rememberAsyncImagePainter
import com.bulat.newsaggregator.R
import com.bulat.newsaggregator.core.domain.model.NewsItem
import com.bulat.newsaggregator.ui.theme.NewsAggregatorTheme

@Composable
fun NewsListItem(item: NewsItem, onClick: () -> Unit) {
    val formattedDescription = HtmlCompat.fromHtml(item.description, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                painter = rememberAsyncImagePainter(item.imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
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
                modifier = Modifier
                    .fillMaxWidth(),
                text = formattedDescription,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewNewsItem() = NewsAggregatorTheme {

    val newsItem = NewsItem(
        title = "Title",
        description = "description",
        imageUrl = "https://i.natgeofe.com/n/548467d8-c5f1-4551-9f58-6817a8d2c45e/NationalGeographic_2572187_16x9.jpg?w=1200",
        link = "",
        date = "06.04.2025",
        author = "Author",
        tags = listOf("cat")
    )

    NewsListItem(
        newsItem
    ) { }
}