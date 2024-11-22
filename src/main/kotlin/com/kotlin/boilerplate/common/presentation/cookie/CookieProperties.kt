package com.kotlin.boilerplate.common.presentation.cookie

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.cookie")
data class CookieProperties(
    val maxAge: Long,
    val path: String,
    val sameSite: String,
    val domain: String,
    val httpOnly: Boolean,
    val secure: Boolean,
) {
}