package com.giussepr.memecreator.meme_editor.presentation.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.giussepr.memecreator.meme_editor.presentation.MemeText
import kotlin.math.roundToInt

class MemeRenderCalculator(
    private val density: Float
) {
    companion object {
        private const val TEXT_PADDING_DP = 8f
        private const val STROKE_WIDTH_DP = 3f
    }

    fun calculateScaleFactors(
        bitmapWidth: Int,
        bitmapHeight: Int,
        templateSize: IntSize
    ): ScaleFactors {
        val scaleX = if (templateSize.width > 0) (bitmapWidth.toFloat() / templateSize.width) else 1f
        val scaleY = if (templateSize.height > 0) (bitmapHeight.toFloat() / templateSize.height) else 1f

        val bitmapScale = (scaleX + scaleY) / 2

        return ScaleFactors(
            scaleX = scaleX,
            scaleY = scaleY,
            bitmapScale = bitmapScale
        )
    }

    fun calculateScaledMemeText(
        memeText: MemeText,
        scaleFactors: ScaleFactors,
        templateSize: IntSize
    ): ScaledMemeText {
        val (scaleX, scaleY, bitmapScale) = scaleFactors

        val scaledOffset = Offset(
            x = (memeText.offsetRatioX * templateSize.width) * scaleX,
            y = (memeText.offsetRatioY * templateSize.height) * scaleY
        )

        val textPaddingPx = TEXT_PADDING_DP * density
        val textPaddingBitmapX = textPaddingPx * scaleX
        val textPaddingBitmapY = textPaddingPx * scaleY

        val scaledFontSize = memeText.fontSize * bitmapScale

        val strokeWidth = STROKE_WIDTH_DP * density * scaleX

        val paddingDp = TEXT_PADDING_DP * 2
        val paddingPx = paddingDp * density
        val constraintWidth = ((templateSize.width / memeText.scale) * scaleX - paddingPx * scaleX)
            .roundToInt()
            .coerceAtLeast(1)

        return ScaledMemeText(
            text = memeText.text,
            scaledOffset = scaledOffset,
            scaledFontSizeSizePx = scaledFontSize.toPx(),
            strokeWidth = strokeWidth,
            constraintWidth = constraintWidth,
            textPaddingX = textPaddingBitmapX,
            textPaddingY = textPaddingBitmapY,
            rotation = memeText.rotation,
            scale = memeText.scale,
            originalText = memeText
        )
    }
}

data class ScaleFactors(
    val scaleX: Float,
    val scaleY: Float,
    val bitmapScale: Float
)
