package com.mdshahsamir.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun EbTextView(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle,
    maxLines: Int = Int.MAX_VALUE,
    maxCharacter: Int = Int.MAX_VALUE,
    overflow: TextOverflow,
    color: Color = Color.Unspecified,
) {
    Text(
        modifier = modifier,
        text = if (text.length > maxCharacter) text.substring(0, maxCharacter) + "..." else text,
        style = style,
        maxLines = maxLines,
        overflow = overflow,
        color = color,
    )
}
