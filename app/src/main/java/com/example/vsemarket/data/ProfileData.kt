package com.example.vsemarket.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "users")
data class ProfileData(
    @PrimaryKey val userId: String,
    val userName: String,
    val email: String,
    val phone: String?,
    val avatarUri: String? = null
)