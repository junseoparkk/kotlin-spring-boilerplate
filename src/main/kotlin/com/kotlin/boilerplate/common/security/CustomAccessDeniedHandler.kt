package com.kotlin.boilerplate.common.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class CustomAccessDeniedHandler : AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        ex: AccessDeniedException
    ) {
        response.status = UNAUTHORIZED.value()
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