package dev.giussepr.memecreator.meme_editor.data

import dev.giussepr.memecreator.meme_editor.domain.SaveToStorageStrategy

expect class CacheStorageStrategy: SaveToStorageStrategy {
    override fun getFilePath(fileName: String): String
}
