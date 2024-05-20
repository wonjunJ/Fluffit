package com.kiwa.fluffit.presentation

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FluffitWearApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}