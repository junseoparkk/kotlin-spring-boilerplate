package com.kotlin.boilerplate.auth.infrastructure.jwt

import com.kotlin.boilerplate.common.exception.CustomSecurityException
import com.kotlin.boilerplate.user.domain.repository.UserRepository
import io.jsonwebtoken.Claims
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationProvider(
    private val userRepository: UserRepository,
    private val jwtTokenProvider: JwtTokenProvider,
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {
        val beforeToken = authentication as JwtAuthenticationToken
        val accessToken = beforeToken.getAccessToken()

        try {
            if (!jwtTokenProvider.validateJwt(accessToken)) {
                throw CustomSecurityException("[ERROR] Invalid AccessToken")
            }
            val userId = jwtTokenProvider.getClaim(accessToken, Claims::getSubject).toLong()
            val user = userRepository.findByIdOrNull(userId) ?: throw CustomSecurityException("")
            return JwtAuthenticationToken.afterOf(user)
        } catch (ex: RuntimeException) {
            throw CustomSecurityException("[ERROR] Invalid AccessToken")
        }
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return JwtAuthenticationToken::class.java.isAssignableFrom(authentication)
    }
}