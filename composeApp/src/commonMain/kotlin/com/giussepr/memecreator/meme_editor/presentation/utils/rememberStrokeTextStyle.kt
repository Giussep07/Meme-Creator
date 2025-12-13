package com.giussepr.memecreator.meme_editor.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.giussepr.memecreator.core.theme.Fonts

@Composable
fun rememberStrokeTextStyle(
    fontSize: TextUnit = 36.sp,
    fontFamily: FontFamily = Fonts.Impact,
    strokeColor: Color = Color.Black,
    strokeWidth: Dp = 3.dp,
    textAlign: TextAlign = TextAlign.Center
): TextStyle {
    val density = LocalDensity.current
    return remember(fontSize, fontFamily, strokeWidth, strokeColor, textAlign) {
        TextStyle(
            color = strokeColor,
            textAlign = textAlign,
            fontSize = fontSize,
            fontFamily = fontFamily,
            drawStyle = Stroke(
                width = with(density) { strokeWidth.toPx() },
                miter = 10f,
                join = StrokeJoin.Round
            )
        )
    }
}
