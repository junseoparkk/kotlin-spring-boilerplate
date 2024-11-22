package com.kotlin.boilerplate.common.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.kotlin.boilerplate.common.exception.CustomSecurityException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationEntryPoint : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        failed: AuthenticationException
    ) {
        val ex = failed as CustomSecurityException

        response.status = HttpStatus.UNAUTHORIZED.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        try {
            response.outputStream.use { os ->
                ObjectMapper().writeValue(os, ex.message)
                os.flush()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}