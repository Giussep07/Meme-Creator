package com.giussepr.memecreator

import androidx.compose.runtime.Composable
import com.giussepr.memecreator.core.presentation.NavigationRoot
import com.giussepr.memecreator.core.theme.MemeCreatorTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MemeCreatorTheme {
        NavigationRoot()
    }
}