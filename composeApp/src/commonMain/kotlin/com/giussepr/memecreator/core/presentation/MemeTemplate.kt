package com.giussepr.memecreator.core.presentation

import memecreator.composeapp.generated.resources.Res
import memecreator.composeapp.generated.resources.allDrawableResources
import org.jetbrains.compose.resources.DrawableResource

data class MemeTemplate(
    val id: String,
    val drawable: DrawableResource
)

val memeTemplates = Res
    .allDrawableResources
    .filterKeys { it.startsWith("meme_template") }
    .map {
        MemeTemplate(
            id = it.key,
            drawable = it.value
        )
    }
