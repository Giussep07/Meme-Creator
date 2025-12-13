@file:OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)

package com.giussepr.memecreator.meme_editor.data

import androidx.compose.ui.unit.IntSize
import com.giussepr.memecreator.meme_editor.domain.MemeExporter
import com.giussepr.memecreator.meme_editor.domain.SaveToStorageStrategy
import com.giussepr.memecreator.meme_editor.presentation.MemeText
import com.giussepr.memecreator.meme_editor.presentation.utils.MemeRenderCalculator
import com.giussepr.memecreator.meme_editor.presentation.utils.ScaledMemeText
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.useContents
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import platform.CoreGraphics.CGContextRef
import platform.CoreGraphics.CGContextRestoreGState
import platform.CoreGraphics.CGContextRotateCTM
import platform.CoreGraphics.CGContextSaveGState
import platform.CoreGraphics.CGContextScaleCTM
import platform.CoreGraphics.CGContextTranslateCTM
import platform.CoreGraphics.CGFloat
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSData
import platform.Foundation.NSNumber
import platform.Foundation.NSString
import platform.Foundation.create
import platform.Foundation.writeToFile
import platform.UIKit.NSFontAttributeName
import platform.UIKit.NSForegroundColorAttributeName
import platform.UIKit.NSLineBreakByWordWrapping
import platform.UIKit.NSMutableParagraphStyle
import platform.UIKit.NSParagraphStyleAttributeName
import platform.UIKit.NSStrokeColorAttributeName
import platform.UIKit.NSStrokeWidthAttributeName
import platform.UIKit.NSTextAlignmentCenter
import platform.UIKit.UIColor
import platform.UIKit.UIFont
import platform.UIKit.UIGraphicsBeginImageContextWithOptions
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetCurrentContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIScreen
import platform.UIKit.boundingRectWithSize
import platform.UIKit.drawWithRect
import kotlin.math.PI

actual class PlatformMemeExporter : MemeExporter {

    private val memeRenderCalculator = MemeRenderCalculator(
        density = UIScreen.mainScreen.scale.toFloat()
    )

    actual override suspend fun exportMeme(
        backgroundImageBytes: ByteArray,
        memeTexts: List<MemeText>,
        templateSize: IntSize,
        saveToStorageStrategy: SaveToStorageStrategy,
        fileName: String
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val backgroundImage = createBackgroundImage(
                imageBytes = backgroundImageBytes
            ) ?: return@withContext Result.failure(Exception("Failed to create background image"))
            val outputImage = renderMeme(
                backgroundImage = backgroundImage,
                memeTexts = memeTexts,
                templateSize = templateSize
            ) ?: return@withContext Result.failure(Exception("Failed to create output image"))

            saveMemeToFile(
                image = outputImage,
                fileName = fileName,
                saveToStorageStrategy = saveToStorageStrategy
            )
        } catch (e: Exception) {
            coroutineContext.ensureActive()
            Result.failure(e)
        }
    }

    private fun saveMemeToFile(
        image: UIImage,
        fileName: String,
        saveToStorageStrategy: SaveToStorageStrategy
    ): Result<String> {
        val jpegData = UIImageJPEGRepresentation(image, 90.0)
            ?: return Result.failure(Exception("Failed to create JPEG image"))

        val filePath = saveToStorageStrategy.getFilePath(fileName)
        val saved = jpegData.writeToFile(filePath, atomically = true)

        return if(saved) {
            Result.success(filePath)
        } else {
            Result.failure(Exception("Failed to save file."))
        }
    }

    private fun createBackgroundImage(imageBytes: ByteArray): UIImage? {
        val imageData = imageBytes.usePinned { pinned ->
            NSData.Companion.create(
                bytes = pinned.addressOf(0),
                length = imageBytes.size.toULong()
            )
        }
        return UIImage.imageWithData(imageData)
    }

    private fun renderMeme(
        backgroundImage: UIImage,
        memeTexts: List<MemeText>,
        templateSize: IntSize
    ): UIImage? {
        val imageSize = IntSize(
            width = backgroundImage.size.useContents { width.toInt() },
            height = backgroundImage.size.useContents { height.toInt() }
        )

        UIGraphicsBeginImageContextWithOptions(
            CGSizeMake(imageSize.width.toDouble(), imageSize.height.toDouble()),
            false,
            0.0
        )

        val context = UIGraphicsGetCurrentContext()
        if(context == null) {
            UIGraphicsEndImageContext()
            return null
        }

        backgroundImage.drawInRect(
            CGRectMake(
                x = 0.0,
                y = 0.0,
                width = imageSize.width.toDouble(),
                height = imageSize.height.toDouble()
            )
        )

        val scaleFactors = memeRenderCalculator.calculateScaleFactors(
            bitmapWidth = imageSize.width,
            bitmapHeight = imageSize.height,
            templateSize = templateSize
        )
        val scaledMemeTexts = memeTexts.map { memeText ->
            memeRenderCalculator.calculateScaledMemeText(
                memeText = memeText,
                scaleFactors = scaleFactors,
                templateSize = templateSize
            )
        }

        scaledMemeTexts.forEach { memeText ->
            drawText(
                context = context,
                memeText = memeText
            )
        }

        val resultImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()

        return resultImage
    }

    private fun drawText(context: CGContextRef, memeText: ScaledMemeText) {
        val textNS = NSString.create(memeText.text)
        val attributes = createMemeTextAttributes(
            fontSize = memeText.scaledFontSizeSizePx,
            strokeWidth = memeText.strokeWidth
        )

        val boundingRect = textNS?.boundingRectWithSize(
            size = CGSizeMake(memeText.constraintWidth.toDouble(), CGFloat.MAX_VALUE),
            options = 1L shl 0,
            attributes = attributes,
            context = null
        ) ?: return

        val textHeight = boundingRect.useContents { size.height.toFloat() }
        val textWidth = boundingRect.useContents { size.width.toFloat() }

        val boxWidth = textWidth + memeText.textPaddingX * 2
        val boxHeight = textHeight + memeText.textPaddingY * 2

        val centerX = memeText.scaledOffset.x + boxWidth / 2
        val centerY = memeText.scaledOffset.y + boxHeight / 2

        CGContextSaveGState(context)
        CGContextTranslateCTM(context, centerX.toDouble(), centerY.toDouble())
        CGContextScaleCTM(context, memeText.scale.toDouble(), memeText.scale.toDouble())
        CGContextRotateCTM(context, memeText.rotation * PI / 180.0)

        val textCenteringOffset = (memeText.constraintWidth - textWidth) / 2f
        CGContextTranslateCTM(
            context,
            (-boxWidth / 2f + memeText.textPaddingX - textCenteringOffset).toDouble(),
            (-boxHeight / 2f + memeText.textPaddingY).toDouble(),
        )

        textNS.drawWithRect(
            rect = CGRectMake(0.0, 0.0, memeText.constraintWidth.toDouble(), textHeight.toDouble()),
            options = 1L shl 0,
            attributes = attributes,
            context = null
        )

        CGContextRestoreGState(context)
    }

    private fun createMemeTextAttributes(
        fontSize: Float,
        strokeWidth: Float
    ): Map<Any?, Any?> {
        val font = UIFont.fontWithName("Impact", fontSize.toDouble())
            ?: UIFont.boldSystemFontOfSize(fontSize.toDouble())

        val paragraphStyle = NSMutableParagraphStyle().apply {
            setAlignment(NSTextAlignmentCenter)
            setLineBreakMode(NSLineBreakByWordWrapping)
        }

        return mapOf(
            NSFontAttributeName to font,
            NSForegroundColorAttributeName to UIColor.whiteColor,
            NSStrokeColorAttributeName to UIColor.blackColor,
            NSStrokeWidthAttributeName to NSNumber(-strokeWidth),
            NSParagraphStyleAttributeName to paragraphStyle
        )
    }
}
