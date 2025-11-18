package com.example.fooddelivery.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Модель профиля пользователя.
 * Содержит основную информацию о пользователе (имя, email и т.д.).
 */

@Entity(tableName = "users")
data class ProfileData(
    @PrimaryKey val userId: String,
    val userName: String,
    val email: String,
    val phone: String?,
    val avatarUri: String? = null,

    val isEmailVerified: Boolean = false
)
