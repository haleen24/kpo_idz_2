package ru.hse.restaurant.data.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("user")
data class User(
    @Id
    var id: Int = 0,

    @Column("login")
    var login: String = "",

    @Column("password")
    var password: String = "",

    @Column("root")
    var root: String = ""
)
