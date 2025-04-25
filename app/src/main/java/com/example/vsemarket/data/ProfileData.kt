package com.example.vsemarket.data

import com.google.gson.annotations.SerializedName

data class ProfileData(
    @SerializedName("avatarUri") val avatarUri: String? = null,
    @SerializedName("username") val username: String = "Имя пользователя",
    @SerializedName("email") val email: String = "email@example.com",
    @SerializedName("phone") val phone: String? = null
)