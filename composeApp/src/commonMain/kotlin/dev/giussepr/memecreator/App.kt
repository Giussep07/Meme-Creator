package dev.giussepr.memecreator

import androidx.compose.runtime.Composable
import dev.giussepr.memecreator.core.presentation.NavigationRoot
import dev.giussepr.memecreator.core.theme.MemeCreatorTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MemeCreatorTheme {
        NavigationRoot()
    }
}