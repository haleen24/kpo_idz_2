package ru.hse.restaurant.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.SecurityFilterChain
import ru.hse.restaurant.service.restaurant.RestaurantUserDetailsService


@EnableWebSecurity
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfiguration {
    @Throws(Exception::class)

    @Bean
    fun users(): UserDetailsService = RestaurantUserDetailsService()

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http.cors {
            it.disable()
        }.csrf {
            it.disable()
        }.authorizeHttpRequests {
            it.requestMatchers("/public/css/*", "/login*").permitAll()
                .requestMatchers("/admin**").hasRole("ADMIN")
                .anyRequest().authenticated()
        }.formLogin {
            it.loginPage("/login").successForwardUrl("/restaurant")
        }.logout {
        }.userDetailsService(users())
            .build()
    }

}