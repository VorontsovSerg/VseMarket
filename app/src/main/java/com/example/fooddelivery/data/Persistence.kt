package com.example.fooddelivery.data

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.UUID

/**
 * Объект для управления сохранением и загрузкой данных профиля, корзины, истории поиска и заказов.
 */
object Persistence {
    private const val PROFILE_PREFS = "profile_prefs"
    private const val KEY_PROFILE = "profile"
    private const val SEARCH_HISTORY_PREFS = "search_history_prefs"
    private const val SEARCH_HISTORY_KEY = "search_history"
    private const val CART_PREFS = "cart_prefs"
    private const val CART_KEY = "cart"
    private const val ORDERS_PREFS = "orders_prefs"
    private const val ORDERS_KEY = "orders"

    private val gson = Gson()

    /**
     * Сохраняет данные профиля в SharedPreferences.
     */
    fun saveProfile(context: Context, profile: ProfileData) {
        val prefs = context.getSharedPreferences(PROFILE_PREFS, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val json = gson.toJson(profile)
        editor.putString(KEY_PROFILE, json)
        editor.apply()
    }

    /**
     * Загружает данные профиля из SharedPreferences или Firebase, если локальные данные отсутствуют.
     */
    fun loadProfile(context: Context): ProfileData? {
        val prefs = context.getSharedPreferences(PROFILE_PREFS, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_PROFILE, null)
        return if (json != null) {
            gson.fromJson(json, ProfileData::class.java)
        } else {
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            firebaseUser?.let {
                ProfileData(
                    userId = it.uid,
                    userName = it.displayName ?: "Гость",
                    email = it.email ?: "example@email.com",
                    phone = it.phoneNumber,
                    avatarUri = it.photoUrl?.toString(),
                    isEmailVerified = it.isEmailVerified
                )
            }
        }
    }

    /**
     * Очищает данные профиля из SharedPreferences.
     */
    fun clearProfile(context: Context) {
        val prefs = context.getSharedPreferences(PROFILE_PREFS, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_PROFILE).apply()
    }

    /**
     * Сохраняет историю поиска в SharedPreferences (максимум 10 записей).
     */
    fun saveSearchHistory(context: Context, history: List<String>) {
        val prefs = context.getSharedPreferences(SEARCH_HISTORY_PREFS, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val json = gson.toJson(history.take(10))
        editor.putString(SEARCH_HISTORY_KEY, json)
        editor.apply()
    }

    /**
     * Загружает историю поиска из SharedPreferences.
     */
    fun loadSearchHistory(context: Context): List<String> {
        val prefs = context.getSharedPreferences(SEARCH_HISTORY_PREFS, Context.MODE_PRIVATE)
        val json = prefs.getString(SEARCH_HISTORY_KEY, null) ?: return emptyList()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    /**
     * Очищает историю поиска из SharedPreferences.
     */
    fun clearSearchHistory(context: Context) {
        val prefs = context.getSharedPreferences(SEARCH_HISTORY_PREFS, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }

    /**
     * Сохраняет элементы корзины в SharedPreferences.
     */
    fun saveCart(context: Context, cartItems: List<CartItem>) {
        val prefs = context.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val json = gson.toJson(cartItems)
        editor.putString(CART_KEY, json)
        editor.apply()
    }

    /**
     * Загружает элементы корзины из SharedPreferences.
     */
    fun loadCart(context: Context): List<CartItem> {
        val prefs = context.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE)
        val json = prefs.getString(CART_KEY, null) ?: return emptyList()
        val type = object : TypeToken<List<CartItem>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    /**
     * Сохраняет список заказов в SharedPreferences.
     */
    fun saveOrders(context: Context, orders: List<Order>) {
        val prefs = context.getSharedPreferences(ORDERS_PREFS, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val json = gson.toJson(orders)
        editor.putString(ORDERS_KEY, json)
        editor.apply()
    }

    /**
     * Загружает список заказов из SharedPreferences.
     */
    fun loadOrders(context: Context): List<Order> {
        val prefs = context.getSharedPreferences(ORDERS_PREFS, Context.MODE_PRIVATE)
        val json = prefs.getString(ORDERS_KEY, null) ?: return emptyList()
        val type = object : TypeToken<List<Order>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    /**
     * Очищает список заказов из SharedPreferences.
     */
    fun clearOrders(context: Context) {
        val prefs = context.getSharedPreferences(ORDERS_PREFS, Context.MODE_PRIVATE)
        prefs.edit().remove(ORDERS_KEY).apply()
    }
}
