package com.kotlin.boilerplate.common.exception

import org.springframework.security.core.AuthenticationException

class CustomSecurityException(
    message: String,
) : AuthenticationException(message)