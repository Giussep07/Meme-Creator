package com.giussepr.memecreator.meme_editor.presentation

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

data class MemeText(
    val id: String,
    val text: String,
    val fontSize: TextUnit = 36.sp,
    val offsetRatioX: Float = 0f,
    val offsetRatioY: Float = 0f,
    val rotation: Float = 0f,
    val scale: Float = 1f
)
