package com.kotlin.boilerplate.common.presentation.response

import java.time.LocalDateTime

data class ApiResponse<T>(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val requestDetails: String,
    val status: Int,
    val message: String,
    val data: T?
)
