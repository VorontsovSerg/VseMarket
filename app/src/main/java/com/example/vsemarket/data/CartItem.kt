package com.example.vsemarket.data

/**
 * Модель элемента корзины.
 * Содержит информацию о товаре, добавленном в корзину, и его количестве.
 */

data class CartItem(val product: Product, var quantity: Int)