package ru.hse.restaurant.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import ru.hse.restaurant.service.api.RestaurantSystemApi

@Controller
@RequestMapping("login")
class LoginController {

    @Autowired
    lateinit var restaurantApplication: RestaurantSystemApi

    @RequestMapping("")
    fun login(): String = "login"

    @GetMapping("sign_up")
    fun addUser(): String = "add_user"

    @PostMapping("create_user")
    fun addUser(@RequestParam username: String, @RequestParam password: String): String {

        return if (username == "" || password == "" || !restaurantApplication.addUser(username, password, "USER")) {
            "add_user"

        } else "login"

    }

    @PostMapping("admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun addAdmin(@RequestParam username: String, @RequestParam password: String): String {
        return if (username == "" || password == "" || !restaurantApplication.addUser(username, password, "ADMIN")) {
            "add_admin"

        } else "redirect:/restaurant"
    }

    @GetMapping("admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun addAdmin() = "add_admin"

}