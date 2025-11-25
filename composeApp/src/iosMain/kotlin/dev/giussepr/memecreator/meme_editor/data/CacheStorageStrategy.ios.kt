package dev.giussepr.memecreator.meme_editor.data

import dev.giussepr.memecreator.meme_editor.domain.SaveToStorageStrategy

actual class CacheStorageStrategy :
    SaveToStorageStrategy {
    actual override fun getFilePath(fileName: String): String {
        TODO("Not yet implemented")
    }
}