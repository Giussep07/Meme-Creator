package com.giussepr.memecreator.meme_editor.presentation.utils

import androidx.compose.ui.unit.TextUnit
import platform.UIKit.UIScreen

actual fun TextUnit.toPx(): Float {
    return this.value * UIScreen.mainScreen.scale.toFloat()
}
