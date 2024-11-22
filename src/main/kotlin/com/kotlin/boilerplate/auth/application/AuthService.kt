package com.kotlin.boilerplate.auth.application

import com.kotlin.boilerplate.auth.application.dto.AuthTokenResponse
import com.kotlin.boilerplate.auth.domain.AuthToken
import com.kotlin.boilerplate.auth.domain.AuthTokenRepository
import com.kotlin.boilerplate.auth.infrastructure.jwt.JwtTokenProvider
import com.kotlin.boilerplate.user.domain.repository.UserRepository
import io.jsonwebtoken.Claims
import org.springframework.stereotype.Service

@Service
class AuthService(
    val userRepository: UserRepository,
    val tokenRepository: AuthTokenRepository,
    val jwtTokenProvider: JwtTokenProvider,
) {

    fun login(userId: Long): AuthTokenResponse {
        val findUser = userRepository.findById(userId).orElseThrow()
        val authToken = AuthToken.create(findUser.id)
        tokenRepository.save(authToken)

        val accessToken = jwtTokenProvider.generateAccessToken(authToken.userId, emptyList())
        val refreshToken = jwtTokenProvider.generateRefreshToken(authToken.tokenId)
        return AuthTokenResponse(accessToken, refreshToken)
    }

    fun reissueAuthToken(token: String): AuthTokenResponse {
        val tokenId = jwtTokenProvider.getClaim(token, Claims::getSubject)
        val findToken = tokenRepository.findByTokenId(tokenId) ?: throw IllegalArgumentException()
        tokenRepository.deleteByTokenId(findToken.tokenId)

        val newToken = AuthToken.create(findToken.userId)
        tokenRepository.save(newToken)

        val accessToken = jwtTokenProvider.generateAccessToken(newToken.userId, emptyList())
        val refreshToken = jwtTokenProvider.generateRefreshToken(newToken.tokenId)
        return AuthTokenResponse.of(accessToken, refreshToken)
    }

    fun logout(userId: Long, token: String) {
        val tokenId = jwtTokenProvider.getClaim(token, Claims::getSubject)
        val findToken = tokenRepository.findByTokenId(tokenId) ?: throw IllegalArgumentException()
        findToken.isMatchOrElseThrow(findToken.userId)
        tokenRepository.deleteByTokenId(findToken.tokenId)
    }
}