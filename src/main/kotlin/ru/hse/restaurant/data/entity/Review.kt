package ru.hse.restaurant.data.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column

data class Review(
    @Id
    var id: Int = -1,

    @Column("user_id")
    var userId: Int = -1,

    @Column("order_id")
    var orderId: Int = -1,

    @Column("dish_id")
    var dishId: Int = -1,

    @Column("rate")
    var rate: Int = -1,

    @Column("review")
    var review: String = ""
)