package ru.hse.restaurant.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import ru.hse.restaurant.etc.getCustomUserDetails
import ru.hse.restaurant.service.api.RestaurantSystemApi

@Controller
@RequestMapping("order")
@PreAuthorize("isAuthenticated()")
class OrderController {
    @Autowired
    lateinit var restaurantApplication: RestaurantSystemApi

    @GetMapping("create")
    fun createOrder(model: Model): String {

        val handler = getCustomUserDetails()

        restaurantApplication.addOrderToKitchen(restaurantApplication.addOrder(handler.getId()))

        return viewOrders(model)
    }

    @GetMapping("view")
    fun viewOrders(model: Model): String {

        val handler = getCustomUserDetails()

        val allOrders = restaurantApplication.findAllOrders(handler.getId())

        model.addAttribute(
            "orders",
            allOrders.map { it to restaurantApplication.findAllDishesByOrderId(it.id) }
        )

        return "orders"
    }

    @GetMapping("cancel")
    fun cancelOrder(@RequestParam("order_id") orderId: Int, model: Model): String {

        if (orderToUserVerification(orderId)) {

            restaurantApplication.cancelOrder(orderId)

        }

        return viewOrders(model)
    }

    @GetMapping("payment")
    fun getPayment(@RequestParam("id") orderId: Int, model: Model): String {

        if (orderToUserVerification(orderId)) {

            restaurantApplication.updateStatusByOrderId(orderId, "payed")

        }

        return viewOrders(model)
    }

    fun orderToUserVerification(orderId: Int): Boolean {

        val order = restaurantApplication.getOrder(orderId)

        return order != null && order.userId == getCustomUserDetails().getId()
    }
}