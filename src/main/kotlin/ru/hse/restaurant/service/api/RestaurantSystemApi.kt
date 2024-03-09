package ru.hse.restaurant.service.api


import ru.hse.restaurant.data.entity.*
import ru.hse.restaurant.data.storage.Cart


interface RestaurantSystemApi {

    fun findAllDishes(): Iterable<Dish>

    fun addUser(username: String, password: String, root: String): Boolean

    fun addDish(name: String, amountOf: Int, price: Int, seconds: Int): Boolean

    fun updateAmountOfDishById(id: Int, amountOf: Int)

    fun deleteDishById(id: Int)

    fun findDishById(id: Int): Dish?

    fun addToCart(userId: Int, dishId: Int, quantity: Int)

    fun removeFromCart(userId: Int, dishId: Int, quantity: Int)

    fun closeCart(userId: Int)

    fun getCart(userId: Int): Cart

    fun addOrder(userId: Int): Int

    fun findAllOrders(userId: Int): List<Order>

    fun findAllDishesByOrderId(orderId: Int): List<OrderToDishJoinWithDish>

    fun addOrderToKitchen(userId: Int)

    fun cancelOrder(orderId: Int)

    fun updateStatusByOrderId(orderId: Int, status: String)

    fun getOrder(orderId: Int): Order?

    fun leaveReview(userId: Int, dishId: Int, orderId: Int, rate: Int, review: String)

    fun getAllReviewsOnDish(dishId: Int): List<Review>

    fun getDishName(dishId: Int): String?

    fun removeReviewById(reviewId: Int)

}