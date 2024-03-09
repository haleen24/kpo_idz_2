package ru.hse.restaurant.data.entity

import org.springframework.data.relational.core.mapping.Column

data class OrderToDishJoinWithDish(
    @Column("user_id")
    var userId: Int = -1,

    @Column("order_id")
    var orderId: Int = -1,

    @Column("dish_id")
    var dishId: Int = -1,

    @Column("quantity")
    var quantity: Int = -1,

    @Column("name")
    var name: String = ""
)