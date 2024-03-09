package ru.hse.restaurant.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import ru.hse.restaurant.data.repository.MenuRepository
import ru.hse.restaurant.data.repository.UserRepository

@PreAuthorize("hasRole('ROLE_ADMIN')")
@Controller
@RequestMapping("stats")
class StatController {
    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var menuRepository: MenuRepository

    val mapper: ObjectWriter = ObjectMapper().writer().withDefaultPrettyPrinter()

    @RequestMapping("")
    fun getInfo(model: Model): String {

        model.addAttribute("users", mapper.writeValueAsString(userRepository.getData()).toString())

        model.addAttribute("top_dishes", mapper.writeValueAsString(menuRepository.getAllWithQuantity()).toString())

        return "stats"
    }

}