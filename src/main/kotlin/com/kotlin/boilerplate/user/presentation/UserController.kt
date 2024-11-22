package com.kotlin.boilerplate.user.presentation

import com.kotlin.boilerplate.user.application.UserService
import com.kotlin.boilerplate.user.application.dto.UserSignupRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
) {

    @PostMapping("/signup")
    fun signUp(@RequestBody request: UserSignupRequest): ResponseEntity<Unit> {
        return ResponseEntity.ok(userService.signUp(request))
    }
}