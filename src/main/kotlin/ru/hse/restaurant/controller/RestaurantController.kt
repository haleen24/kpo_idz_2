package ru.hse.restaurant.controller

import ru.hse.restaurant.etc.getCustomUserDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import ru.hse.restaurant.service.api.RestaurantSystemApi
import kotlin.RuntimeException


@Controller
@RequestMapping("restaurant")
@PreAuthorize("isAuthenticated()")
class RestaurantController {

    @Autowired
    private lateinit var restaurantApplication: RestaurantSystemApi

    @RequestMapping("")
    fun index(model: Model): String {

        val name = SecurityContextHolder.getContext().authentication.name

        model.addAttribute("name", name)

        return "index"
    }


    @GetMapping("menu")
    fun viewMenu(model: Model): String {

        val handler = getCustomUserDetails()

        model.addAttribute("user_id", handler.getId())

        model.addAttribute("dishes", restaurantApplication.findAllDishes())

        model.addAttribute("order", restaurantApplication.getCart(handler.getId()).getAll())

        return "menu"
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("menu/add")
    fun addDishHTML() = "menu_add"

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("menu/add")
    fun addDishHandler(
        @RequestParam name: String,
        @RequestParam("amount_of") amountOf: Int,
        @RequestParam price: Int,
        @RequestParam seconds: Int,
        model: Model
    ): String {

        return try {
            restaurantApplication.addDish(name, amountOf, price, seconds)

            viewMenu(model)

        } catch (ex: RuntimeException) {

            "add_dish_error"

        }
    }


    @PostMapping("menu/change_amount_of")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun changeAmountOfDish(
        @RequestParam("id") id: Int,
        @RequestParam("quantity") value: Int, model: Model
    ): String {

        val dish = restaurantApplication.findDishById(id) ?: return viewMenu(model)

        if (value == 0) {

            restaurantApplication.deleteDishById(id)

        } else if (value > 0) {

            restaurantApplication.updateAmountOfDishById(dish.id, value)

        }

        return viewMenu(model)
    }


    @PostMapping("menu/cart/set")
    fun changeInCart(
        @RequestParam("dish_id") id: Int,
        @RequestParam("quantity") quantity: Int,
        model: Model
    ): String {

        if (quantity < 0) {

            return viewMenu(model)

        } else if (quantity == 0) {

            restaurantApplication.removeFromCart(getCustomUserDetails().getId(), id, quantity)

        }

        restaurantApplication.addToCart(getCustomUserDetails().getId(), id, quantity)

        return viewMenu(model)
    }

}
