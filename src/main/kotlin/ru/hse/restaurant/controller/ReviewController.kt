package ru.hse.restaurant.controller

import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import ru.hse.restaurant.etc.getCustomUserDetails
import ru.hse.restaurant.service.api.RestaurantSystemApi

@Controller
@RequestMapping("review")
class ReviewController {

    @Autowired
    lateinit var restaurantApplication: RestaurantSystemApi

    @GetMapping("")
    fun review(
        @RequestParam("order_id") orderId: Int,
        @RequestParam("dish_id") dishId: Int, model: Model
    ): String {

        model.addAttribute("order_id", orderId)

        model.addAttribute("dish_id", dishId)

        return "leave_review"
    }

    @PostMapping("")
    fun review(
        @RequestParam("order_id") orderId: Int,
        @RequestParam("dish_id") dishId: Int,
        @RequestParam("review") review: String,
        @RequestParam("rate") rate: Int
    ): String {

        val handler = getCustomUserDetails()

        val order = restaurantApplication.getOrder(orderId) ?: return "redirect:/restaurant"

        if (order.status != "payed" || order.userId != handler.getId() || rate < 1 || rate > 5
            || review.length > 256
        ) {

            return "review_error"

        }

        restaurantApplication.leaveReview(order.userId, dishId, orderId, rate, review)

        return "redirect:/restaurant"
    }

    @GetMapping("view")
    fun checkReview(
        @RequestParam("id") dishId: Int,
        model: Model
    ): String {

        val name = restaurantApplication.getDishName(dishId) ?: return "redirect:/restaurant/menu"

        model.addAttribute("name", name)

        model.addAttribute("reviews", restaurantApplication.getAllReviewsOnDish(dishId))

        return "reviews"
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/remove")
    fun removeReview(
        @RequestParam("id") reviewId: Int,
        request: HttpServletRequest
    ): String {

        restaurantApplication.removeReviewById(reviewId)

        return "redirect:/restaurant"
    }

}