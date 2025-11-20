package dev.giussepr.memecreator.meme_editor.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import dev.giussepr.memecreator.core.theme.Fonts

@Composable
fun rememberFillTextStyle(
    fontSize: TextUnit = 36.sp,
    fontFamily: FontFamily = Fonts.Impact,
    fillColor: Color = Color.White,
    textAlign: TextAlign = TextAlign.Center
): TextStyle {
    return remember(fontSize, fontFamily, fillColor, textAlign) {
        TextStyle(
            color = fillColor,
            textAlign = textAlign,
            fontSize = fontSize,
            fontFamily = fontFamily
        )
    }
}
