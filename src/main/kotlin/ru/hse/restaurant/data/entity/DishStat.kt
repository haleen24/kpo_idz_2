package ru.hse.restaurant.data.entity

import org.springframework.data.relational.core.mapping.Column

data class DishStat(

    @Column("id")
    val id: Int = -1,

    @Column("name")
    val name: String = "",

    @Column("amount_of")
    var quantity: Int = -1,

    @Column("avg_rate")
    val avgRate: Double? = null,

    @Column("revenue")
    val revenue: Int = -1
)