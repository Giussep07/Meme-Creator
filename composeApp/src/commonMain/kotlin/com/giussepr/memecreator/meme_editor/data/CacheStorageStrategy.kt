package com.giussepr.memecreator.meme_editor.data

import com.giussepr.memecreator.meme_editor.domain.SaveToStorageStrategy

expect class CacheStorageStrategy: SaveToStorageStrategy {
    override fun getFilePath(fileName: String): String
}
