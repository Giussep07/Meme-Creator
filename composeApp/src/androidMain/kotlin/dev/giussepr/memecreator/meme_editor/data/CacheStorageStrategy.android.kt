package dev.giussepr.memecreator.meme_editor.data

import android.content.Context
import dev.giussepr.memecreator.meme_editor.domain.SaveToStorageStrategy
import java.io.File

actual class CacheStorageStrategy(
    private val context: Context
) : SaveToStorageStrategy {
    actual override fun getFilePath(fileName: String): String {
        return File(context.cacheDir, fileName).absolutePath
    }
}
