package dev.giussepr.memecreator.meme_editor.presentation.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File

actual class PlatformShareSheet(
    private val context: Context
) {
    actual fun shareFile(filePath: String) {
        val file = File(filePath)

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/jpeg"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        val chooser = Intent.createChooser(intent, null).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(chooser)
    }
}
