package com.giussepr.memecreator

import android.app.Application
import com.giussepr.memecreator.di.initKoin
import org.koin.android.ext.koin.androidContext

class MemeCreatorApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MemeCreatorApplication)
        }
    }
}
