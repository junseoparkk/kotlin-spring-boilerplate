package com.kotlin.boilerplate.common.logging

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper

private val log = KotlinLogging.logger {}

@Component
class ReqResLoggingFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val cachingRequestWrapper = ContentCachingRequestWrapper(request)
        val cachingResponseWrapper = ContentCachingResponseWrapper(response)

        val startTime = System.currentTimeMillis()
        filterChain.doFilter(cachingRequestWrapper, cachingResponseWrapper)
        val endTime = System.currentTimeMillis()
        val elapsedTime = (endTime - startTime) / 1000.0

        try {
            log.info {
                HttpLogMessage.create(
                    requestWrapper = cachingRequestWrapper,
                    responseWrapper = cachingResponseWrapper,
                    elapsedTime = elapsedTime
                ).toPrettierLog()
            }
            cachingResponseWrapper.copyBodyToResponse()
        } catch (ex: Exception) {
            log.error(ex) { "[${this::class.simpleName}] Failure Logging: ${ex.message}" }
        }
    }
}