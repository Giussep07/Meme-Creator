package dev.giussepr.memecreator.meme_editor.data

import androidx.compose.ui.unit.IntSize
import dev.giussepr.memecreator.meme_editor.domain.MemeExporter
import dev.giussepr.memecreator.meme_editor.domain.SaveToStorageStrategy
import dev.giussepr.memecreator.meme_editor.presentation.MemeText

actual class PlatformMemeExporter : MemeExporter {
    actual override suspend fun exportMeme(
        backgroundImageBytes: ByteArray,
        memeTexts: List<MemeText>,
        templateSize: IntSize,
        saveToStorageStrategy: SaveToStorageStrategy,
        fileName: String
    ): Result<String> {
        TODO("Not yet implemented")
    }
}
