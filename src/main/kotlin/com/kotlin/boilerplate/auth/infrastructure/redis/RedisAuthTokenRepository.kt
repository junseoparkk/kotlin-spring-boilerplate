package com.kotlin.boilerplate.auth.infrastructure.redis

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.kotlin.boilerplate.auth.domain.AuthToken
import com.kotlin.boilerplate.auth.domain.AuthTokenRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RedisAuthTokenRepository(
    private val objectMapper: ObjectMapper,
    private val redisTemplate: RedisTemplate<String, String>,
) : AuthTokenRepository {

    override fun save(token: AuthToken) {
        val key = generateKey(token.tokenId)
        val value = serializeToken(token)
        redisTemplate.opsForValue()[key, value, TTL] = TimeUnit.MINUTES
    }

    override fun deleteByTokenId(tokenId: String) {
        redisTemplate.delete(generateKey(tokenId))
    }

    override fun findByTokenId(tokenId: String): AuthToken? {
        return redisTemplate.opsForValue()[generateKey(tokenId)]
            ?.let { deserializeToken(it) }
    }

    private fun generateKey(tokenId: String): String {
        return TOKEN_PREFIX + tokenId
    }

    private fun serializeToken(token: AuthToken): String {
        try {
            return objectMapper.writeValueAsString(token)
        } catch (ex: JsonProcessingException) {
            throw IllegalStateException("[ERROR] Impossible to serialize")
        }
    }

    private fun deserializeToken(token: String): AuthToken {
        try {
            return objectMapper.readValue(token, AuthToken::class.java)
        } catch (ex: JsonProcessingException) {
            throw IllegalStateException("[ERROR] Impossible to deserialize")
        }
    }

    companion object {
        private const val TTL = 10_080L
        private const val TOKEN_PREFIX = "token:"
    }
}