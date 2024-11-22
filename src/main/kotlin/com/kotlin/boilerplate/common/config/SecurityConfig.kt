package com.kotlin.boilerplate.common.config

import com.kotlin.boilerplate.auth.application.AuthService
import com.kotlin.boilerplate.auth.infrastructure.jwt.JwtTokenProvider
import com.kotlin.boilerplate.common.security.FilterSkipMatcher
import com.kotlin.boilerplate.common.security.CustomAccessDeniedHandler
import com.kotlin.boilerplate.common.security.CustomAuthenticationEntryPoint
import com.kotlin.boilerplate.auth.infrastructure.jwt.JwtAuthenticationFilter
import com.kotlin.boilerplate.auth.infrastructure.jwt.JwtAuthenticationProvider
import com.kotlin.boilerplate.auth.infrastructure.login.LoginAuthenticationFilter
import com.kotlin.boilerplate.auth.infrastructure.login.LoginAuthenticationProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.RequestMatcher

@Configuration
@EnableWebSecurity
class SecurityConfig (
    private val authService: AuthService,
    private val jwtTokenProvider: JwtTokenProvider,
    private val jwtAuthenticationProvider: JwtAuthenticationProvider,
    private val loginAuthenticationProvider: LoginAuthenticationProvider,
    private val accessDeniedHandler: CustomAccessDeniedHandler,
    private val authenticationEntryPoint: CustomAuthenticationEntryPoint,
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .securityMatcher("/**")
            .cors { it.disable() }
            .csrf { it.disable() }
            .logout { it.disable() }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }

            .authorizeHttpRequests { request -> request
                .requestMatchers("/error").permitAll()
                .requestMatchers("/api/v1/health").permitAll()
                .requestMatchers("/api/v1/users/signup").permitAll()
                .requestMatchers("/api/v1/users/login").permitAll()
                .requestMatchers("/api/v1/auth/reissue").permitAll()
                .requestMatchers("/oauth2/authorization/**").permitAll()
                .anyRequest().authenticated()
            }

//            .oauth2Login { oauth2 -> oauth2
//                .successHandler()
//                .failureHandler()
//                .userInfoEndpoint {
//                    it.userService()
//                }
//                .authorizationEndpoint {
//                    it.authorizationRequestRepository()
//                }
//            }

            .addFilterBefore(loginAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .addFilterAfter(jwtAuthenticationFilter(), LoginAuthenticationFilter::class.java)
            .exceptionHandling { handler -> handler.accessDeniedHandler(accessDeniedHandler) }
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    private fun loginAuthenticationFilter(): LoginAuthenticationFilter {
        val providerManager = ProviderManager(loginAuthenticationProvider)
        val filter = LoginAuthenticationFilter("/api/v1/users/login", jwtTokenProvider, authService)
        filter.setAuthenticationManager(providerManager)
        filter.setAuthenticationFailureHandler(AuthenticationEntryPointFailureHandler(authenticationEntryPoint))
        return filter
    }

    private fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
        val providerManager = ProviderManager(jwtAuthenticationProvider)
        val filter = JwtAuthenticationFilter(generatedRequestMatcher())
        filter.setAuthenticationManager(providerManager)
        filter.setAuthenticationFailureHandler(AuthenticationEntryPointFailureHandler(authenticationEntryPoint))
        return filter
    }

    private fun generatedRequestMatcher(): RequestMatcher {
        return FilterSkipMatcher(
            listOf(
                "/api/v1/health",
                "/api/v1/users/signup",
                "/api/v1/users/login",
                "/api/v1/auth/reissue",
                "/oauth2/authorization/kakao",
            )
        )
    }
}