package com.kotlin.boilerplate.common.presentation

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class HealthCheckControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    @DisplayName("서버가 정상 상태인 경우 200OK와 ok 메시지를 반환한다.")
    fun should_ReturnSuccessStatusAndMessage_When_ServerIsHealthy() {
        // given
        val expectedResponse = """{"message":"ok"}"""

        // when & then
        mockMvc.perform(get("/api/v1/health"))
            .andExpect(status().isOk)
            .andExpect(content().json(expectedResponse))
    }
}