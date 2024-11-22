package com.kotlin.boilerplate.auth.domain

interface AuthTokenRepository {

    fun save(token: AuthToken)

    fun deleteByTokenId(tokenId: String)

    fun findByTokenId(tokenId: String): AuthToken?
}