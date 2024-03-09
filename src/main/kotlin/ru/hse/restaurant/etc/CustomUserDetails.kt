package ru.hse.restaurant.etc

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails

@Throws(RuntimeException::class)
fun getCustomUserDetails(): CustomUserDetails {
    val handler = SecurityContextHolder.getContext().authentication.principal
    if (handler !is CustomUserDetails) {
        throw RuntimeException("The handler is not CustomUserDetails")
    }
    return handler
}

class CustomUserDetails(private val user: UserDetails, private val id: Int) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = user.authorities

    override fun getPassword(): String = user.password

    override fun getUsername(): String = user.username

    override fun isAccountNonExpired(): Boolean = user.isAccountNonExpired

    override fun isAccountNonLocked(): Boolean = user.isAccountNonLocked

    override fun isCredentialsNonExpired(): Boolean = user.isCredentialsNonExpired

    override fun isEnabled(): Boolean = user.isEnabled

    fun getId() = id

}