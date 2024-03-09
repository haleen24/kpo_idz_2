package ru.hse.restaurant.etc

import ru.hse.restaurant.data.entity.Order
import ru.hse.restaurant.dto.DishInfoStorage


class OrderBuilder : Builder<Map<String, DishInfoStorage>, Order> {
    private val order = Order()

    override fun addDishes(collectionIn: Map<String, DishInfoStorage>): OrderBuilder {

        for ((_, info) in collectionIn) {

            order.price += info.dish.price * info.quantity

            order.time += info.dish.seconds * info.quantity
        }

        return this
    }

    override fun setUserId(userId: Int): Builder<Map<String, DishInfoStorage>, Order> {

        order.userId = userId

        return this
    }

    override fun setStatus(status: String): Builder<Map<String, DishInfoStorage>, Order> {

        order.status = status

        return this
    }

    override fun build(): Order = order
}