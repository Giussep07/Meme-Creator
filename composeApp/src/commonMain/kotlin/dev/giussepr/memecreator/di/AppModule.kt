package dev.giussepr.memecreator.di

import dev.giussepr.memecreator.meme_editor.presentation.MemeEditorViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::MemeEditorViewModel)
}
