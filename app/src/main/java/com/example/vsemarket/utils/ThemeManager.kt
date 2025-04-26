package com.example.vsemarket.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import com.example.vsemarket.MainActivity

/**
 * Менеджер темы приложения.
 * Управляет переключением между светлой и тёмной темой в зависимости от настроек пользователя.
 * Предоставляет функции для применения и сохранения состояния темы.
 */

object ThemeManager {
    private const val PREFS_NAME = "theme_prefs"
    private const val KEY_THEME = "is_dark_theme"

    fun saveTheme(context: Context, isDarkTheme: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_THEME, isDarkTheme).apply()
    }

    fun isDarkTheme(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_THEME, false)
    }

    fun restartActivity(activity: Activity) {
        val intent = Intent(activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
        activity.finish()
    }

    @Composable
    fun FoodDeliveryTheme(
        isDarkTheme: Boolean,
        content: @Composable () -> Unit
    ) {
        val colorScheme = if (isDarkTheme) {
            darkColorScheme(
                primary = Color(0xFF0288D1),
                onPrimary = Color.White,
                primaryContainer = Color(0xFF0288D1),
                onPrimaryContainer = Color.White,
                background = Color(0xFF1C1C1C),
                onBackground = Color.White,
                surface = Color(0xFF1C1C1C),
                onSurface = Color.White
            )
        } else {
            lightColorScheme(
                primary = Color(0xFF03A9F4),
                onPrimary = Color.White,
                primaryContainer = Color(0xFF03A9F4),
                onPrimaryContainer = Color.White,
                background = Color.White,
                onBackground = Color.Black,
                surface = Color.White,
                onSurface = Color.Black
            )
        }

        MaterialTheme(
            colorScheme = colorScheme,
            typography = MaterialTheme.typography,
            content = content
        )
    }
}