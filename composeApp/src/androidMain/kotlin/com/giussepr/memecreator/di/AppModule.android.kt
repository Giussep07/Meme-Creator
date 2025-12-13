package com.giussepr.memecreator.di

import com.giussepr.memecreator.meme_editor.data.CacheStorageStrategy
import com.giussepr.memecreator.meme_editor.data.PlatformMemeExporter
import com.giussepr.memecreator.meme_editor.domain.MemeExporter
import com.giussepr.memecreator.meme_editor.domain.SaveToStorageStrategy
import com.giussepr.memecreator.meme_editor.presentation.utils.PlatformShareSheet
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformAppModule = module {
    factoryOf(::PlatformMemeExporter) bind MemeExporter::class
    factoryOf(::CacheStorageStrategy) bind SaveToStorageStrategy::class
    factoryOf(::PlatformShareSheet)
}