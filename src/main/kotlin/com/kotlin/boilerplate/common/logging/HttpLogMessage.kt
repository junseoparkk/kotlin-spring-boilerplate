package com.kotlin.boilerplate.common.logging

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper

data class HttpLogMessage(
    val httpMethod: String,
    val requestUri: String,
    val httpStatus: HttpStatus,
    val clientIp: String,
    val elapsedTime: Double,
    val requestHeaders: String?,
    val responseHeaders: String?,
    val requestParam: String?,
    val requestBody: String?,
    val responseBody: String?,
) {
    companion object {
        fun create(
            requestWrapper: ContentCachingRequestWrapper,
            responseWrapper: ContentCachingResponseWrapper,
            elapsedTime: Double
        ): HttpLogMessage {
            return HttpLogMessage(
                httpMethod = requestWrapper.method,
                requestUri = requestWrapper.requestURI,
                httpStatus = HttpStatus.valueOf(responseWrapper.status),
                clientIp = requestWrapper.remoteAddr,
                elapsedTime = elapsedTime,
                requestHeaders = this.getRequestHeaders(requestWrapper),
                responseHeaders = this.getResponseHeaders(responseWrapper),
                requestParam = this.getRequestParams(requestWrapper),
                requestBody = String(requestWrapper.contentAsByteArray),
                responseBody = String(responseWrapper.contentAsByteArray)
            )
        }

        private fun getRequestHeaders(request: ContentCachingRequestWrapper): String {
            val headerNames = request.headerNames.toList()
            return headerNames.joinToString(separator = ", ") { headerName ->
                val headerValue = request.getHeader(headerName)
                "$headerName: $headerValue"
            }
        }

        private fun getResponseHeaders(response: ContentCachingResponseWrapper): String {
            val headerNames = response.headerNames
            return headerNames.joinToString(separator = ", ") { headerName ->
                val headerValues = response.getHeaders(headerName)
                "$headerName: ${headerValues.joinToString(", ")}"
            }
        }

        private fun getRequestParams(request: ContentCachingRequestWrapper): String {
            val paramsMap = request.parameterMap.mapValues { it.value.joinToString(", ") }
            return jacksonObjectMapper().writeValueAsString(paramsMap)
        }
    }

    fun toPrettierLog(): String {
        return """
        |
        |[REQUEST] ${this.httpMethod} ${this.requestUri} (${this.elapsedTime}s)
        |>> CLIENT_IP: ${this.clientIp}
        |>> REQUEST_HEADERS: ${this.requestHeaders}
        |>> RESPONSE_HEADERS: ${this.requestHeaders}
        |>> REQUEST_PARAM: ${this.requestParam}
        |>> REQUEST_BODY: ${truncateBody(this.requestBody)}
        |>> RESPONSE_BODY: ${truncateBody(this.responseBody)}
        """.trimMargin()
    }

    private fun truncateBody(body: String?, maxLength: Int = 500): String {
        return body?.take(maxLength) + if ((body?.length ?: 0) > maxLength) "..." else ""
    }

}
