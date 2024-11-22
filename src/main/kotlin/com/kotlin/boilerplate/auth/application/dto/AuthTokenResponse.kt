package com.kotlin.boilerplate.auth.application.dto

import net.minidev.json.annotate.JsonIgnore

data class AuthTokenResponse(
    val accessToken: String,
    @JsonIgnore val refreshToken: String,
) {

    companion object {
        fun of(accessToken: String, refreshToken: String): AuthTokenResponse {
            return AuthTokenResponse(accessToken, refreshToken)
        }
    }
}
