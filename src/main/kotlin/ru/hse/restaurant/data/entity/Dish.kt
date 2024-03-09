package ru.hse.restaurant.data.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("menu")
data class Dish(
    @Id
    val id: Int = 0,
    @Column("name")
    val name: String = "",
    @Column("amount_of")
    var amountOf: Int = 0,
    @Column("price")
    val price: Int = 0,
    @Column("seconds")
    val seconds: Int = 0
)