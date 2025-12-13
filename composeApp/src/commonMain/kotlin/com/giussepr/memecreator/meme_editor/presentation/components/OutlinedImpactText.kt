package com.giussepr.memecreator.meme_editor.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.giussepr.memecreator.core.theme.MemeCreatorTheme
import com.giussepr.memecreator.meme_editor.presentation.utils.rememberFillTextStyle
import com.giussepr.memecreator.meme_editor.presentation.utils.rememberStrokeTextStyle
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun OutlinedImpactText(
    modifier: Modifier = Modifier,
    text: String,
    strokeTextStyle: TextStyle,
    fillTextStyle: TextStyle
) {
    Box(
        modifier = modifier
    ) {
        Text(
            text = text,
            style = strokeTextStyle
        )
        Text(
            text = text,
            style = fillTextStyle
        )
    }
}

@Composable
@Preview
private fun OutlinedImpactTextPreview() {
    MemeCreatorTheme {
        OutlinedImpactText(
            text = "HELLO WORLD!",
            strokeTextStyle = rememberStrokeTextStyle(),
            fillTextStyle = rememberFillTextStyle()
        )
    }
}
