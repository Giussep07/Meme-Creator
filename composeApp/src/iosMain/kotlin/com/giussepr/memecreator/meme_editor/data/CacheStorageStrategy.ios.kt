package com.giussepr.memecreator.meme_editor.data

import com.giussepr.memecreator.meme_editor.domain.SaveToStorageStrategy
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

actual class CacheStorageStrategy : SaveToStorageStrategy {
    actual override fun getFilePath(fileName: String): String {
        val cacheDirectory = NSSearchPathForDirectoriesInDomains(
            NSCachesDirectory,
            NSUserDomainMask,
            true
        ).firstOrNull() as? String ?: throw IllegalStateException("Could not find cache directory")

        return "$cacheDirectory/$fileName"
    }
}