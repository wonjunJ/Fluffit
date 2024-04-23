package com.kiwa.fluffit

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FluffitApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}