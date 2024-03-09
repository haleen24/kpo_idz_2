package ru.hse.restaurant.etc.encoder

import org.springframework.security.crypto.password.PasswordEncoder
import ru.hse.restaurant.service.restaurant.RestaurantSystem.Companion.checkPassword
import ru.hse.restaurant.service.restaurant.RestaurantSystem.Companion.encryption

class EncoderAdapter : PasswordEncoder {
    override fun encode(rawPassword: CharSequence) =
        encryption(rawPassword.toString())


    override fun matches(rawPassword: CharSequence, encodedPassword: String): Boolean =
        checkPassword(rawPassword.toString(), encodedPassword)
}
