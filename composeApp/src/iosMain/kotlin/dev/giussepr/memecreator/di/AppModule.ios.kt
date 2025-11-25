package dev.giussepr.memecreator.di

import dev.giussepr.memecreator.meme_editor.data.CacheStorageStrategy
import dev.giussepr.memecreator.meme_editor.data.PlatformMemeExporter
import dev.giussepr.memecreator.meme_editor.domain.MemeExporter
import dev.giussepr.memecreator.meme_editor.domain.SaveToStorageStrategy
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformAppModule = module {
    factoryOf(::PlatformMemeExporter) bind MemeExporter::class
    factoryOf(::CacheStorageStrategy) bind SaveToStorageStrategy::class
}
