package io.github.maikotrindade.stockapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class StockApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}