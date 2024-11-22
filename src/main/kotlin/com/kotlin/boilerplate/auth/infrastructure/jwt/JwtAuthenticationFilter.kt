package com.kotlin.boilerplate.auth.infrastructure.jwt

import com.kotlin.boilerplate.common.exception.CustomSecurityException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.RequestMatcher

class JwtAuthenticationFilter(
    matcher: RequestMatcher,
) : AbstractAuthenticationProcessingFilter(matcher) {

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val accessToken = request.getHeader(HEADER_NAME)
            ?.takeIf { it.startsWith(HEADER_PREFIX) }
            ?.substring(HEADER_PREFIX.length)
            ?: throw CustomSecurityException("[ERROR] Not Found AccessToken")
        val beforeToken = JwtAuthenticationToken.beforeOf(accessToken)
        return super.getAuthenticationManager().authenticate(beforeToken)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authentication: Authentication
    ) {
        val afterToken = authentication as JwtAuthenticationToken
        val securityContext = SecurityContextHolder.createEmptyContext()
        securityContext.authentication = afterToken
        SecurityContextHolder.setContext(securityContext)
        chain.doFilter(request, response)
    }

    override fun unsuccessfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        failed: AuthenticationException
    ) {
        SecurityContextHolder.clearContext()
        super.getFailureHandler().onAuthenticationFailure(request, response, failed)
    }

    companion object {
        const val HEADER_NAME = "Authorization"
        const val HEADER_PREFIX = "Bearer "
    }
}