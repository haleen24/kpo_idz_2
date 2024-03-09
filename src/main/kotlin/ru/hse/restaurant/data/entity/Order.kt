package ru.hse.restaurant.data.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("order")
class Order {
    @Id
    var id: Int = -1

    @Column("user_id")
    var userId: Int = -1

    @Column("status")
    var status: String = ""

    @Column("price")
    var price: Int = 0

    @Column("time")
    var time: Int = 0


    fun union(order: Order): Order {
        id = if (id == -1) order.id else id

        price += order.price

        time += order.time


        return this
    }

}