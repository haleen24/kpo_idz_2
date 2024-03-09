package ru.hse.restaurant.data.storage

import ru.hse.restaurant.data.entity.Dish

interface OrderStorage {

    fun setQuantityToCart(userId: Int, dish: Dish, quantity: Int)

    fun deleteFromCart(userId: Int, dishName: String, quantity: Int)

    fun getCart(userId: Int): Cart

    fun closeCart(userId: Int)
}