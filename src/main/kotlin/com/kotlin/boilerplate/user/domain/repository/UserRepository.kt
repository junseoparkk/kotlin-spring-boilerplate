package com.kotlin.boilerplate.user.domain.repository

import com.kotlin.boilerplate.user.domain.UserType
import com.kotlin.boilerplate.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email: String): User?

    fun findByName(name: String): User?

    fun findByUserTypeAndSocialId(userType: UserType, socialId: String): User?
}