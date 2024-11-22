package com.kotlin.boilerplate.common.presentation.cookie

import jakarta.servlet.http.Cookie
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseCookie.ResponseCookieBuilder
import org.springframework.stereotype.Component
import java.util.*

@Component
class CookieHandler(
    private val cookieProperties: CookieProperties,
) {

    fun createCookie(cookieKey: String, cookieValue: String): ResponseCookie {
        return createCookieWithMaxAge(cookieKey, cookieValue, cookieProperties.maxAge)
    }

    fun deleteCookie(cookieKey: String): ResponseCookie {
        return createCookieWithMaxAge(cookieKey, EMPTY_COOKIE_VALUE, EMPTY_COOKIE_AGE)
    }

    private fun createCookieWithMaxAge(cookieKey: String, cookieValue: String, maxAge: Long): ResponseCookie {
        val cookieBuilder = ResponseCookie.from(cookieKey, cookieValue)
            .maxAge(maxAge)
            .path(cookieProperties.path)
            .sameSite(cookieProperties.sameSite)
            .secure(cookieProperties.secure)
            .httpOnly(cookieProperties.httpOnly)
        return setDomainIfNotLocal(cookieBuilder)
    }

    private fun setDomainIfNotLocal(cookieBuilder: ResponseCookieBuilder): ResponseCookie {
        if (cookieProperties.domain != LOCALHOST) {
            cookieBuilder.domain(cookieProperties.domain)
        }
        return cookieBuilder.build()
    }

    companion object {
        private const val EMPTY_COOKIE_AGE = 0L
        private const val EMPTY_COOKIE_VALUE = ""
        private const val LOCALHOST = "localhost"
        private const val REFRESH_TOKEN = "refresh_token"

        fun createCookieWithAge(cookieKey: String, expireDate: Date): Cookie {
            return Cookie(REFRESH_TOKEN, cookieKey).apply {
                isHttpOnly = true
                maxAge = ((expireDate.time - System.currentTimeMillis()) / 1000).toInt()
            }
        }
    }
}