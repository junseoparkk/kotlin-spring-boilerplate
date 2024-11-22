package com.kotlin.boilerplate.auth.application.dto

data class UserLoginRequest(
    val email: String = "",
    val password: String = "",
)
