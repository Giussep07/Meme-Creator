package dev.giussepr.memecreator

import androidx.compose.runtime.Composable
import dev.giussepr.memecreator.core.theme.MemeCreatorTheme
import dev.giussepr.memecreator.meme_gallery.presentation.MemeGalleryScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MemeCreatorTheme {
        MemeGalleryScreen(onMemeTemplateSelected = {

        })
    }
}