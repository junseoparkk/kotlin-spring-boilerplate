package com.kotlin.boilerplate.common.presentation.response

import com.kotlin.boilerplate.common.exception.ErrorResponse
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@RestControllerAdvice
class GlobalApiResponseAdvice : ResponseBodyAdvice<Any> {

    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>
    ): Boolean =
        MappingJackson2HttpMessageConverter::class.java.isAssignableFrom(converterType)

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        if (request.uri.path == "/error") {
            return body
        }

        val responseDetails = "[${request.method}] ${request.uri.path}"

        return when (body) {
            is ErrorResponse -> body
            else -> ApiResponse(
                requestDetails = responseDetails,
                status = 200,
                message = "success",
                data = body
            )
        }
    }
}