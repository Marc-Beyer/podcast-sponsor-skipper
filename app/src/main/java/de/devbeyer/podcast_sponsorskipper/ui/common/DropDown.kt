package de.devbeyer.podcast_sponsorskipper.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun DropDown(
    text: String,
    expanded: Boolean,
    onExpandedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current


    IconButton(
        modifier = modifier.fillMaxWidth(),
        onClick = { onExpandedChanged(!expanded) }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Filter",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Icon(
                imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChanged(false) }
        ) {
            content()
        }
    }
}