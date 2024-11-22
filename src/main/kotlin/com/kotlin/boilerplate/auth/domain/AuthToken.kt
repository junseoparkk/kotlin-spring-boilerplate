package com.kotlin.boilerplate.auth.domain

import java.util.*

data class AuthToken(
    val userId: Long,
    val tokenId: String,
) {

    fun isMatchOrElseThrow(userId: Long) {
        if (this.userId != userId) {
            throw SecurityException("[ERROR] Not Found User")
        }
    }

    companion object {
        fun create(userId: Long): AuthToken {
            return AuthToken(userId, generateTokenId())
        }

        private fun generateTokenId(): String {
            return UUID.randomUUID().toString()
        }
    }
}