package dev.giussepr.memecreator.meme_editor.presentation.utils

import androidx.compose.ui.geometry.Offset
import dev.giussepr.memecreator.meme_editor.presentation.MemeText

data class ScaledMemeText(
    val text: String,
    val scaledOffset: Offset,
    val scaledFontSizeSizePx: Float,
    val strokeWidth: Float,
    val constraintWidth: Int,
    val textPaddingX: Float,
    val textPaddingY: Float,
    val rotation: Float,
    val scale: Float,
    val originalText: MemeText
)
