package ru.hse.restaurant.service.restaurant

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.relational.core.sql.LockMode
import org.springframework.data.relational.repository.Lock
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.interceptor.TransactionAspectSupport
import ru.hse.restaurant.data.entity.Dish
import ru.hse.restaurant.data.entity.Order
import ru.hse.restaurant.data.entity.OrderToDishJoinWithDish
import ru.hse.restaurant.data.entity.Review
import ru.hse.restaurant.data.repository.*
import ru.hse.restaurant.data.storage.Cart
import java.security.MessageDigest
import java.util.*
import kotlin.RuntimeException
import ru.hse.restaurant.data.storage.SessionOrderStorage
import ru.hse.restaurant.data.storage.OrderStorage
import ru.hse.restaurant.service.api.KitchenApi
import ru.hse.restaurant.service.api.RestaurantSystemApi
import kotlin.jvm.optionals.getOrNull

@Component
class RestaurantSystem : RestaurantSystemApi {

    val sessionOrderStorage: OrderStorage = SessionOrderStorage()

    @Autowired
    lateinit var kitchen: KitchenApi

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var menuRepository: MenuRepository

    @Autowired
    lateinit var orderRepository: OrderRepository

    @Autowired
    lateinit var orderToDishRepository: OrderToDishRepository

    @Autowired
    lateinit var reviewRepository: ReviewRepository


    override fun findAllDishes(): Iterable<Dish> = menuRepository.findAll()

    override fun addUser(username: String, password: String, root: String): Boolean {

        return try {

            userRepository.addUser(username, encryption(password), root)

            true

        } catch (_: RuntimeException) {
            false
        }
    }

    override fun addDish(name: String, amountOf: Int, price: Int, seconds: Int): Boolean {

        if (name.isEmpty() || amountOf < 0 || price < 0 || seconds < 0) {
            return false
        }

        return try {

            menuRepository.addDish(name, amountOf, price, seconds)

            true

        } catch (_: RuntimeException) {
            false
        }

    }

    override fun updateAmountOfDishById(id: Int, amountOf: Int) = menuRepository.updateById(id, amountOf)

    override fun deleteDishById(id: Int) = menuRepository.deleteById(id)

    override fun findDishById(id: Int): Dish? = menuRepository.findById(id)

    override fun addToCart(userId: Int, dishId: Int, quantity: Int) {

        val dish = menuRepository.findById(dishId)

        if (dish == null || dish.amountOf < quantity) {
            return
        }

        sessionOrderStorage.setQuantityToCart(userId, dish, quantity)
    }

    override fun removeFromCart(userId: Int, dishId: Int, quantity: Int) {

        val dish = menuRepository.findById(dishId) ?: return

        sessionOrderStorage.deleteFromCart(userId, dish.name, quantity)
    }

    override fun closeCart(userId: Int) = sessionOrderStorage.closeCart(userId)

    override fun getCart(userId: Int): Cart = sessionOrderStorage.getCart(userId)


    companion object {

        fun encryption(password: String): String {

            val md = MessageDigest.getInstance("SHA-256")

            val input = password.toByteArray()

            val bytes = md.digest(input)

            return Base64.getEncoder().encodeToString(bytes)
        }

        fun checkPassword(rawPassword: String, encodedPassword: String) =
            encryption(rawPassword) == encodedPassword
    }

    @Transactional
    @Lock(LockMode.PESSIMISTIC_READ)
    override fun addOrder(userId: Int): Int {

        val cart = sessionOrderStorage.getCart(userId)

        if (cart.getAll().isEmpty()) {
            return -1
        }

        val lastOrder = orderRepository.findLastChangeable(userId)

        val order = cart.getOrder().union(lastOrder ?: Order())

        order.userId = userId

        if (order.id == -1) {

            order.id = orderRepository.insertOrder(order)

        } else {

            orderRepository.update(order)

        }
        try {

            decreaseAllFromMap(sessionOrderStorage.getCart(userId).getMapIdToQuantity())

            for (item in sessionOrderStorage.getCart(userId).getMapIdToQuantity()) {

                orderToDishRepository.insert(order.userId, order.id, item.key, item.value)

            }
        } catch (ex: RuntimeException) {

            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()

            return -1
        }

        sessionOrderStorage.closeCart(userId)

        return order.id
    }

    override fun findAllOrders(userId: Int): List<Order> = orderRepository.findAllByUserId(userId).toList()


    override fun findAllDishesByOrderId(orderId: Int): List<OrderToDishJoinWithDish> =
        orderToDishRepository.findAllDishesByOrderId(orderId)


    override fun addOrderToKitchen(userId: Int) {

        if (userId == -1) {
            return
        }

        kitchen.addOrder(userId)
    }

    override fun cancelOrder(orderId: Int) {

        val res = kitchen.cancelOrder(orderId)

        if (!res) {
            return
        }

        val fullOrder = orderToDishRepository.findAllDishesByOrderId(orderId)

        for (item in fullOrder) {

            menuRepository.updateOnValueById(item.dishId, item.quantity)

        }
    }

    override fun updateStatusByOrderId(orderId: Int, status: String) = orderRepository.updateStatus(orderId, status)

    override fun getOrder(orderId: Int): Order? = orderRepository.findById(orderId).getOrNull()

    override fun leaveReview(userId: Int, dishId: Int, orderId: Int, rate: Int, review: String) =
        reviewRepository.insertReview(userId, dishId, orderId, rate, review)

    fun decreaseAllFromMap(map: Map<Int, Int>) {

        if (menuRepository.findAllId(map.keys.toList()) != map.size) {

            return

        }

        for ((dishId, quantity) in map) {

            menuRepository.updateOnValueById(dishId, -quantity)

        }

        if (menuRepository.findAllLessThanZero() > 0) {

            throw RuntimeException("Transaction canceled")

        }
    }

    override fun getAllReviewsOnDish(dishId: Int): List<Review> = reviewRepository.findAllByDishId(dishId)

    override fun getDishName(dishId: Int): String? = menuRepository.getDishNameById(dishId)

    override fun removeReviewById(reviewId: Int) = reviewRepository.removeById(reviewId)
}