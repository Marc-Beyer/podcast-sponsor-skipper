package de.devbeyer.podcast_sponsorskipper.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.devbeyer.podcast_sponsorskipper.util.Constants

@Composable
fun SettingsIntItem(
    text: String,
    description: String? = null,
    value: String,
    onDone: () -> Unit,
    onValueChange: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.inverseOnSurface)
            .padding(Constants.Dimensions.MEDIUM),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            Modifier.weight(1f),
        ) {
            Text(
                text = text,

                )
            if (!description.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(Constants.Dimensions.SMALL))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Spacer(modifier = Modifier.width(Constants.Dimensions.SMALL))
        TextField(
            value = value,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                onDone()
                keyboardController?.hide()
            }),
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 16.sp,
                lineHeight = 20.sp,
                color = LocalContentColor.current,
                fontWeight = FontWeight.Normal,
            ),
            onValueChange = onValueChange,
            modifier = Modifier
                .width(64.dp)
                .height(48.dp)
                .padding(vertical = 0.dp)
                .onFocusChanged {
                    if (!it.isFocused) {
                        onDone()
                    }
                },
        )
    }
}