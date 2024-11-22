package com.kotlin.boilerplate.auth.infrastructure.jwt

import com.kotlin.boilerplate.user.domain.User
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class JwtAuthenticationToken(
    principal: Any,
    credentials: Any,
    authorities: Collection<GrantedAuthority>? = null,
) : UsernamePasswordAuthenticationToken(principal, credentials, authorities ?: emptyList()) {

    fun getAccessToken(): String {
        return this.principal as String
    }

    companion object {
        fun beforeOf(accessToken: String): JwtAuthenticationToken {
            return JwtAuthenticationToken(accessToken, "")
        }

        fun afterOf(user: User): JwtAuthenticationToken {
            return JwtAuthenticationToken(user.id, "", null)
        }
    }
}