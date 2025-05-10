package com.donait.paymentsimulator.controller

import com.donait.paymentsimulator.IntegrationTest
import com.donait.paymentsimulator.dto.SignupRequestDto
import com.donait.paymentsimulator.dto.LoginRequestDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AuthControllerIntegrationTest : IntegrationTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `회원가입 성공`() {
        val signupRequest = SignupRequestDto(
            username = "testuser",
            password = "password123",
            email = "test@example.com"
        )

        mockMvc.perform(
            post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.message").value("회원가입이 완료되었습니다."))
    }

    @Test
    fun `로그인 성공`() {
        // 회원가입 먼저 수행
        val signupRequest = SignupRequestDto(
            username = "testuser",
            password = "password123",
            email = "test@example.com"
        )

        mockMvc.perform(
            post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest))
        )

        // 로그인 수행
        val loginRequest = LoginRequestDto(
            username = "testuser",
            password = "password123"
        )

        mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.token").exists())
    }

    @Test
    fun `중복된 사용자명으로 회원가입 실패`() {
        val signupRequest = SignupRequestDto(
            username = "testuser",
            password = "password123",
            email = "test@example.com"
        )

        // 첫 번째 회원가입
        mockMvc.perform(
            post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest))
        )

        // 동일한 사용자명으로 두 번째 회원가입 시도
        mockMvc.perform(
            post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message").value("이미 존재하는 사용자명입니다."))
    }
} 