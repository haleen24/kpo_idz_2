package ru.hse.restaurant.service.restaurant

import ru.hse.restaurant.data.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.hse.restaurant.etc.encoder.EncoderAdapter
import ru.hse.restaurant.etc.CustomUserDetails

@Service
class RestaurantUserDetailsService : UserDetailsService {

    private val encoder = EncoderAdapter()

    @Bean
    fun encryptor(): PasswordEncoder = encoder

    @Autowired
    private lateinit var repository: UserRepository

    override fun loadUserByUsername(username: String?): CustomUserDetails {

        username ?: throw UsernameNotFoundException("")

        val tmp = repository.findByName(username) ?: throw UsernameNotFoundException("")

        return CustomUserDetails(User.builder().run {
            username(tmp.login)
            password(tmp.password)
            roles(tmp.root)
            passwordEncoder { it }
            build()
        }, tmp.id)

    }
}