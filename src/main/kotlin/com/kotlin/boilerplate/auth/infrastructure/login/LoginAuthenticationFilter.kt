package com.kotlin.boilerplate.auth.infrastructure.login

import com.fasterxml.jackson.databind.ObjectMapper
import com.kotlin.boilerplate.auth.application.AuthService
import com.kotlin.boilerplate.auth.infrastructure.jwt.JwtTokenProvider
import com.kotlin.boilerplate.common.presentation.cookie.CookieHandler
import com.kotlin.boilerplate.auth.application.dto.UserLoginRequest
import io.jsonwebtoken.Claims
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter

class LoginAuthenticationFilter(
    defaultUrl: String,
    private val jwtTokenProvider: JwtTokenProvider,
    private val authService: AuthService,
) : AbstractAuthenticationProcessingFilter(defaultUrl) {

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val loginRequest = ObjectMapper().readValue(request.reader, UserLoginRequest::class.java)
        val beforeToken = LoginAuthenticationToken.beforeOf(loginRequest)
        return super.getAuthenticationManager().authenticate(beforeToken)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authentication: Authentication
    ) {
        val afterToken = authentication as LoginAuthenticationToken
        val authTokenResponse = authService.login(afterToken.getId().toLong())

        response.addCookie(
            CookieHandler.createCookieWithAge(
                authTokenResponse.refreshToken,
                jwtTokenProvider.getClaim(authTokenResponse.refreshToken, Claims::getExpiration)
            )
        )
        response.status = HttpStatus.OK.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.writer.print(authTokenResponse.accessToken)
    }
}