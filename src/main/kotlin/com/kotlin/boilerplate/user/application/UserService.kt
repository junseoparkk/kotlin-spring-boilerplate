package com.kotlin.boilerplate.user.application

import com.kotlin.boilerplate.user.application.dto.UserSignupRequest
import com.kotlin.boilerplate.user.domain.User
import com.kotlin.boilerplate.user.domain.UserType
import com.kotlin.boilerplate.user.domain.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    @Transactional
    fun signUp(request: UserSignupRequest) {
        validateDuplicated(request)

        val createdUser = User(
            email = request.email,
            password = passwordEncoder.encode(request.password),
            name = request.name,
            type = UserType.GENERAL
        )
        userRepository.save(createdUser)
    }

    fun getUserById(userId: Long): User {
        return userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("[ERROR] Not Exists User") }
    }

    private fun validateDuplicated(request: UserSignupRequest) {
        userRepository.findByEmail(request.email)?.let {
            throw IllegalArgumentException("[ERROR] Email already exists")
        }
        userRepository.findByName(request.name)?.let {
            throw IllegalArgumentException("[ERROR] User name already exists")
        }
    }
}