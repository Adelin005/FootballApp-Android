package com.example.footballapp

import android.app.Application
import com.example.footballapp.data.FavoritesManager
import com.example.footballapp.data.SettingsManager
import com.example.footballapp.data.CacheManager

class FootballApp : Application() {
    companion object {
        lateinit var favoritesManager: FavoritesManager
        lateinit var settingsManager: SettingsManager
        lateinit var cacheManager: CacheManager
    }

    override fun onCreate() {
        super.onCreate()
        favoritesManager = FavoritesManager(this)
        settingsManager = SettingsManager(this)
        cacheManager = CacheManager(this)
    }
}
