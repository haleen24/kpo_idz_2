package ru.hse.restaurant.data.storage

import ru.hse.restaurant.data.entity.Dish
import ru.hse.restaurant.data.entity.Order
import ru.hse.restaurant.dto.DishInfoStorage
import ru.hse.restaurant.etc.OrderBuilder

class SessionCart : Cart {

    private var orderMap = mutableMapOf<String, DishInfoStorage>()

    override fun addDish(dish: Dish, quantity: Int) {

        if (dish.amountOf == 0 || quantity == 0) {

            orderMap.remove(dish.name)

            return
        }

        if (orderMap[dish.name] == null) {

            orderMap[dish.name] = DishInfoStorage(dish, 0)

        }

        if (orderMap[dish.name]!!.quantity < orderMap[dish.name]!!.dish.amountOf) {

            orderMap[dish.name]!!.quantity = quantity
        }
    }

    override fun deleteDish(name: String, quantity: Int) {

        orderMap[name] ?: return

        if (orderMap[name]!!.quantity <= quantity) {
            orderMap.remove(name)
        } else {
            orderMap[name]!!.quantity -= quantity
        }
    }

    override fun getAll(): Map<String, DishInfoStorage> = orderMap

    override fun getMapIdToQuantity(): Map<Int, Int> = orderMap.values.associate { it.dish.id to it.quantity }

    override fun closeCart() {
        orderMap = mutableMapOf()
    }

    override fun getOrder(): Order = OrderBuilder().setStatus("processing").addDishes(orderMap).build()
}