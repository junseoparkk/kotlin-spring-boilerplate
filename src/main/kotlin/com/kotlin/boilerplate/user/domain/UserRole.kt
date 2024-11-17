package com.kotlin.boilerplate.user.domain

enum class UserRole(
    private val key: String,
) {
    GUEST("ROLE_GUEST"),
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    ;
}
