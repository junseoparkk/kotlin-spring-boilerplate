package com.kotlin.boilerplate.auth.infrastructure.jwt

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "security.jwt")
data class JwtProperties(
    val secretKey: String,
    val accessExpiration: Long,
    val refreshExpiration: Long,
)
