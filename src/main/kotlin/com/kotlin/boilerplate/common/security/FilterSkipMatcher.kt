package com.kotlin.boilerplate.common.security

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher

class FilterSkipMatcher(
    pathsToSkip: List<String>,
) : RequestMatcher {

    override fun matches(request: HttpServletRequest): Boolean {
        return !orRequestMatcher.matches(request)
    }

    private val orRequestMatcher: OrRequestMatcher = OrRequestMatcher(
        pathsToSkip.map { AntPathRequestMatcher(it) }
    )
}