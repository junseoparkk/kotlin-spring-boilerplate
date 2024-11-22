package com.kotlin.boilerplate.auth.infrastructure.login

import com.kotlin.boilerplate.common.exception.CustomSecurityException
import com.kotlin.boilerplate.user.domain.User
import com.kotlin.boilerplate.user.domain.repository.UserRepository
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class LoginAuthenticationProvider(
    val userRepository: UserRepository,
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {
        val beforeToken = authentication as LoginAuthenticationToken
        try {
            val user = validate(beforeToken.getEmail(), beforeToken.getPassword())
            return LoginAuthenticationToken.afterOf(user)
        } catch (ex: RuntimeException) {
            println(ex.message)
            throw CustomSecurityException("[ERROR] Invalid user information")
        }
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return LoginAuthenticationToken::class.java.isAssignableFrom(authentication)
    }

    private fun validate(email: String, password: String): User {
        return userRepository.findByEmail(email)
            ?.takeIf { passwordEncoder().matches(password, it.password) }
            ?: throw CustomSecurityException("[ERROR] Not Found User")
    }

    private fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}