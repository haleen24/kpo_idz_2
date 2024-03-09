package ru.hse.restaurant.dto

import ru.hse.restaurant.data.entity.Dish

data class DishInfoStorage(
    val dish: Dish,
    var quantity: Int
)
