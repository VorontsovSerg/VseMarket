package com.example.vsemarket

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {
    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences("theme_prefs", MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean("dark_theme", false)
        applyTheme()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        val sharedPrefs = getSharedPreferences("theme_prefs", MODE_PRIVATE)
        sharedPrefs.edit().putBoolean("dark_theme", darkThemeEnabled).apply()
        applyTheme()
    }

    private fun applyTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}
