package ru.hse.restaurant.data.storage

import ru.hse.restaurant.data.entity.Dish

class SessionOrderStorage : OrderStorage {

    private val sessionCarts: MutableMap<Int, Cart> = mutableMapOf()

    override fun setQuantityToCart(userId: Int, dish: Dish, quantity: Int) {

        if (sessionCarts[userId] == null) {

            sessionCarts[userId] = SessionCart()

        }

        sessionCarts[userId]!!.addDish(dish, quantity)
    }

    override fun deleteFromCart(userId: Int, dishName: String, quantity: Int) {

        sessionCarts[userId] ?: return

        sessionCarts[userId]!!.deleteDish(dishName, quantity)
    }

    override fun getCart(userId: Int): Cart = sessionCarts[userId] ?: SessionCart()

    override fun closeCart(userId: Int) {
        sessionCarts[userId]?.closeCart()
    }

}