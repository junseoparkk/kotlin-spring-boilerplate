package com.kotlin.boilerplate.common.presentation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class HealthCheckController {

    @GetMapping("/health")
    fun healthCheck(): ResponseEntity<HealthResponse> {
        return ResponseEntity.ok(HealthResponse("ok"))
    }

    data class HealthResponse(
        val message: String,
    )
}