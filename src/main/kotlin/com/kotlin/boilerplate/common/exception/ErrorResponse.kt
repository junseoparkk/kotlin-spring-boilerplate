package com.kotlin.boilerplate.common.exception

import java.time.LocalDateTime

data class ErrorResponse(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val requestDetails: String,
    val status: Int,
    val code: String,
    val message: String,
)