package com.bulat.newsaggregator.core.composables.items

import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TagItem(modifier: Modifier = Modifier, tagText: String, isSelected: Boolean, onClick: (String?) -> Unit) {

    AssistChip(
        modifier = modifier,
        onClick = { onClick(tagText) },
        label = { Text(tagText) },
        colors = if (isSelected)
            AssistChipDefaults.assistChipColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
        else AssistChipDefaults.assistChipColors()
    )
}