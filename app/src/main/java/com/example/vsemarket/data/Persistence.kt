package com.example.vsemarket.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Интерфейс для определения методов доступа к данным корзины.
 * Пример репозитория или DAO-слоя для взаимодействия с корзиной.
 */

object Persistence {
    private const val PROFILE_PREFS = "profile_prefs"
    private const val SEARCH_HISTORY_PREFS = "search_history_prefs"
    private const val SEARCH_HISTORY_KEY = "search_history"
    private const val CART_PREFS = "cart_prefs"
    private const val CART_KEY = "cart"

    fun saveProfile(context: Context, profile: ProfileData) {
        val prefs = context.getSharedPreferences(PROFILE_PREFS, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(profile)
        editor.putString("profile", json)
        editor.apply()
    }

    fun loadProfile(context: Context): ProfileData? {
        val prefs = context.getSharedPreferences(PROFILE_PREFS, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = prefs.getString("profile", null) ?: return null
        return gson.fromJson(json, ProfileData::class.java)
    }

    fun saveSearchHistory(context: Context, history: List<String>) {
        val prefs = context.getSharedPreferences(SEARCH_HISTORY_PREFS, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(history.take(10))
        editor.putString(SEARCH_HISTORY_KEY, json)
        editor.apply()
    }

    fun loadSearchHistory(context: Context): List<String> {
        val prefs = context.getSharedPreferences(SEARCH_HISTORY_PREFS, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = prefs.getString(SEARCH_HISTORY_KEY, null) ?: return emptyList()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type)
    }

    fun clearSearchHistory(context: Context) {
        val prefs = context.getSharedPreferences(SEARCH_HISTORY_PREFS, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }

    fun saveCart(context: Context, cartItems: List<CartItem>) {
        val prefs = context.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(cartItems)
        editor.putString(CART_KEY, json)
        editor.apply()
    }

    fun loadCart(context: Context): List<CartItem> {
        val prefs = context.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = prefs.getString(CART_KEY, null) ?: return emptyList()
        val type = object : TypeToken<List<CartItem>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }
}