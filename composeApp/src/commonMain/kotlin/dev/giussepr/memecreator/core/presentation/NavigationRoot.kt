package dev.giussepr.memecreator.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.giussepr.memecreator.meme_editor.presentation.MemeEditorRoot
import dev.giussepr.memecreator.meme_gallery.presentation.MemeGalleryScreen

@Composable
fun NavigationRoot() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Route.MemeGallery
    ) {
        composable<Route.MemeGallery> {
            MemeGalleryScreen(
                onMemeTemplateSelected = { memeTemplate ->
                    navController.navigate(Route.MemeEditor(memeTemplate.id))
                }
            )
        }

        composable<Route.MemeEditor> {
            val templateId = it.toRoute<Route.MemeEditor>().templateId
            val template = remember(templateId) {
                memeTemplates.first { it.id == templateId }
            }
            MemeEditorRoot(
                template = template,
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
    }
}