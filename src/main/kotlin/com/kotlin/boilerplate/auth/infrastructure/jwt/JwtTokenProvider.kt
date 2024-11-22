package com.kotlin.boilerplate.auth.infrastructure.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtTokenProvider(
    private val jwtProperties: JwtProperties,
) {

    private val key: Key = Keys.hmacShaKeyFor(jwtProperties.secretKey.toByteArray())

    fun generateAccessToken(id: Long, authorities: Collection<GrantedAuthority>): String {
        val claims = mapOf(
            "authorities" to authorities.joinToString(",") { it.authority }
        )
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(id.toString())
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + jwtProperties.accessExpiration))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }

    fun generateRefreshToken(id: String): String {
        return Jwts.builder()
            .setSubject(id)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + jwtProperties.refreshExpiration))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }

    fun validateJwt(token: String): Boolean {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
            true
        } catch (ex: Exception) {
            false
        }
    }

    fun <T> getClaim(token: String, func: (Claims) -> T): T {
        val claims: Claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
        return func(claims)
    }
}