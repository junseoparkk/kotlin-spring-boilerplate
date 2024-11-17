package com.kotlin.boilerplate.user.application.dto

data class UserSignupRequest(
    val email: String,
    val password: String,
    val name: String,
)
