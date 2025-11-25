package dev.giussepr.memecreator.meme_editor.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.compose.ui.unit.IntSize
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.withTranslation
import dev.giussepr.memecreator.R
import dev.giussepr.memecreator.meme_editor.domain.MemeExporter
import dev.giussepr.memecreator.meme_editor.domain.SaveToStorageStrategy
import dev.giussepr.memecreator.meme_editor.presentation.MemeText
import dev.giussepr.memecreator.meme_editor.presentation.utils.MemeRenderCalculator
import dev.giussepr.memecreator.meme_editor.presentation.utils.ScaledMemeText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

actual class PlatformMemeExporter(
    private val context: Context
) : MemeExporter {

    private val memeRenderCalculator = MemeRenderCalculator(
        density = context.resources.displayMetrics.density
    )

    actual override suspend fun exportMeme(
        backgroundImageBytes: ByteArray,
        memeTexts: List<MemeText>,
        templateSize: IntSize,
        saveToStorageStrategy: SaveToStorageStrategy,
        fileName: String
    ) = withContext(Dispatchers.IO) {
        var bitmap: Bitmap? = null
        var outputBitmap: Bitmap? = null

        try {
            bitmap = BitmapFactory.decodeByteArray(
                backgroundImageBytes,
                0,
                backgroundImageBytes.size
            )

            outputBitmap = renderMeme(
                background = bitmap,
                memeTexts = memeTexts,
                templateSize = templateSize
            )

            val filePath = saveToStorageStrategy.getFilePath(fileName)
            val file = File(filePath)
            FileOutputStream(file).use { out ->
                outputBitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    90,
                    out
                )
            }

            Result.success(file.absolutePath)
        } catch (e: Exception) {
            coroutineContext.ensureActive()
            Result.failure(e)
        } finally {
            bitmap?.recycle()
            outputBitmap?.recycle()
        }
    }

    private suspend fun renderMeme(
        background: Bitmap,
        memeTexts: List<MemeText>,
        templateSize: IntSize
    ): Bitmap = withContext(Dispatchers.Default) {
        // Make the bitmap mutable
        val output = background.copy(
            Bitmap.Config.ARGB_8888,
            true
        )

        val canvas = Canvas(output)

        val scaleFactors = memeRenderCalculator.calculateScaleFactors(
            bitmapWidth = background.width,
            bitmapHeight = background.height,
            templateSize = templateSize
        )

        val scaledMemeTexts = memeTexts.map {
            memeRenderCalculator.calculateScaledMemeText(
                memeText = it,
                scaleFactors = scaleFactors,
                templateSize = templateSize
            )
        }

        scaledMemeTexts.forEach { scaledMemeText ->
            drawText(canvas, scaledMemeText)
        }

        output
    }

    private fun drawText(canvas: Canvas, memeText: ScaledMemeText) {
        val impactTypeface = ResourcesCompat.getFont(
            context,
            R.font.impact
        ) ?: Typeface.DEFAULT_BOLD

        val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = memeText.strokeWidth
            textSize = memeText.scaledFontSizeSizePx
            typeface = impactTypeface
            color = Color.BLACK
        }

        val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            textSize = memeText.scaledFontSizeSizePx
            typeface = impactTypeface
            color = Color.WHITE
        }

        val strokeLayout = StaticLayout.Builder.obtain(
            memeText.text,
            0,
            memeText.text.length,
            TextPaint(strokePaint),
            memeText.constraintWidth
        )
            .setAlignment(Layout.Alignment.ALIGN_CENTER)
            .setIncludePad(false)
            .build()

        val fillLayout = StaticLayout.Builder.obtain(
            memeText.text,
            0,
            memeText.text.length,
            TextPaint(fillPaint),
            memeText.constraintWidth
        )
            .setAlignment(Layout.Alignment.ALIGN_CENTER)
            .setIncludePad(false)
            .build()

        val textHeight = strokeLayout.height.toFloat()
        val textWidth = (0 until strokeLayout.lineCount)
            .maxOfOrNull { strokeLayout.getLineWidth(it) }
            ?: 0f

        val boxWidth = textWidth + memeText.textPaddingX * 2
        val boxHeight = textHeight + memeText.textPaddingY * 2

        val centerX = memeText.scaledOffset.x + boxWidth / 2f
        val centerY = memeText.scaledOffset.y + boxHeight / 2f

        canvas.withTranslation(centerX, centerY) {
            scale(memeText.scale, memeText.scale)
            rotate(memeText.rotation)

            val textCenteringOffset = (memeText.constraintWidth - textWidth) / 2f
            translate(
                -boxWidth / 2f + memeText.textPaddingX - textCenteringOffset,
                -boxHeight / 2f + memeText.textPaddingY
            )

            strokeLayout.draw(this)
            fillLayout.draw(this)
        }
    }
}
