package com.kotlin.boilerplate.auth.infrastructure.login

import com.kotlin.boilerplate.auth.application.dto.UserLoginRequest
import com.kotlin.boilerplate.user.domain.User
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class LoginAuthenticationToken(
    principal: String,
    credentials: Any,
    authorities: Collection<GrantedAuthority>? = null,
) : UsernamePasswordAuthenticationToken(principal, credentials, authorities ?: emptyList()) {

    fun getEmail(): String {
        return this.principal as String
    }

    fun getPassword(): String {
        return this.credentials as String
    }

    fun getId(): String {
        return this.principal as String
    }

    companion object {
        private const val ERROR_MESSAGE = ""

        fun beforeOf(request: UserLoginRequest): LoginAuthenticationToken {
            return LoginAuthenticationToken(request.email, request.password)
        }

        fun afterOf(user: User): LoginAuthenticationToken {
            return LoginAuthenticationToken(user.id.toString(), "", null)
        }
    }
}