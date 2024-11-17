package com.kotlin.boilerplate.user.domain

import com.kotlin.boilerplate.common.domain.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "`users`")
class User(
    email: String,
    password: String,
    name: String,
    role: UserRole,
    type: UserType,
    socialId: String? = null,
) : BaseEntity() {

    @Column(name = "email", unique = true)
    val email: String = email

    @Column(name = "password")
    var password: String = password
        protected set

    @Column(name = "name")
    var name: String = name
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    val userRole: UserRole = role

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    val userType: UserType = type

    @Column(name = "social_id")
    val socialId: String? = socialId
}