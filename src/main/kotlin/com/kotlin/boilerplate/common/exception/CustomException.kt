package com.kotlin.boilerplate.common.exception

class CustomException(
    message: String,
    val errorMessage: ErrorMessage
) : RuntimeException(message) {

    constructor(errorMessage: ErrorMessage) : this(errorMessage.message, errorMessage)
}