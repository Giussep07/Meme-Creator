package com.giussepr.memecreator.meme_editor.domain

interface SaveToStorageStrategy {
    fun getFilePath(fileName: String): String
}
