package com.kotlin.boilerplate.auth.presentation

import com.kotlin.boilerplate.auth.application.AuthService
import com.kotlin.boilerplate.auth.application.dto.AuthTokenResponse
import com.kotlin.boilerplate.common.presentation.cookie.CookieHandler
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders.SET_COOKIE
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService,
    private val cookieHandler: CookieHandler,
) {

    @PostMapping("/reissue")
    fun reissue(
        @CookieValue(REFRESH_TOKEN) refreshToken: String,
        response: HttpServletResponse
    ): ResponseEntity<AuthTokenResponse> {
        val authTokenResponse = authService.reissueAuthToken(refreshToken)
        val cookie = cookieHandler.createCookie(
            REFRESH_TOKEN, authTokenResponse.refreshToken
        )
        response.addHeader(SET_COOKIE, cookie.toString())
        return ResponseEntity.ok(authTokenResponse)
    }

    @PostMapping("/logout")
    fun logout(
        @AuthenticationPrincipal userId: Long,
        @CookieValue(REFRESH_TOKEN) refreshToken: String,
        response: HttpServletResponse
    ): ResponseEntity<Unit> {
        authService.logout(userId, refreshToken)
        val cookie = cookieHandler.deleteCookie(REFRESH_TOKEN)
        response.addHeader(SET_COOKIE, cookie.toString())
        return ResponseEntity.ok(null)
    }

    companion object {
        private const val REFRESH_TOKEN = "refresh_token"
    }
}