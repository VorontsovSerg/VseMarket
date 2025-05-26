package com.example.vsemarket.data

import java.io.Serializable

data class Order(
    val orderNumber: String,
    val totalPrice: Double,
    val status: String,
    val items: List<CartItem>
) : Serializable