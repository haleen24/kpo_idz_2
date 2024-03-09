package ru.hse.restaurant.data.storage

import ru.hse.restaurant.data.entity.Dish
import ru.hse.restaurant.data.entity.Order
import ru.hse.restaurant.dto.DishInfoStorage

interface Cart {
    fun addDish(dish: Dish, quantity: Int)

    fun deleteDish(name: String, quantity: Int)

    fun getAll(): Map<String, DishInfoStorage>

    fun closeCart()

    fun getOrder(): Order

    fun getMapIdToQuantity(): Map<Int,Int>
}
