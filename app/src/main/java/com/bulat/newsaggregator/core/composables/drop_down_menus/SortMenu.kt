package com.bulat.newsaggregator.core.composables.drop_down_menus

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bulat.newsaggregator.R
import com.bulat.newsaggregator.home.presentation.NewsSortOrder

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