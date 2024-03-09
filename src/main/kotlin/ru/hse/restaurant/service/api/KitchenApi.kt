package ru.hse.restaurant.service.api

interface KitchenApi {

    fun addOrder(orderId: Int)

    fun cancelOrder(orderId: Int): Boolean

    fun getStatus(orderId: Int): String

}