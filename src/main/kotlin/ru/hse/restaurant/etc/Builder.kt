package ru.hse.restaurant.etc

interface Builder<CollectionIn, Out> {

    fun addDishes(collectionIn: CollectionIn): Builder<CollectionIn, Out>

    fun setUserId(userId: Int): Builder<CollectionIn, Out>

    fun setStatus(status: String): Builder<CollectionIn, Out>

    fun build(): Out
}