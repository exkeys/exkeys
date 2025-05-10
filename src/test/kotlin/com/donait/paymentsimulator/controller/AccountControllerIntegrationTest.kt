package com.donait.paymentsimulator.controller

import com.donait.paymentsimulator.IntegrationTest
import com.donait.paymentsimulator.dto.SignupRequestDto
import com.donait.paymentsimulator.dto.LoginRequestDto
import com.donait.paymentsimulator.dto.AccountCreateRequestDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AccountControllerIntegrationTest : IntegrationTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private var authToken: String = ""

    @BeforeEach
    fun setup() {
        // 회원가입
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

        // 로그인하여 토큰 발급
        val loginRequest = LoginRequestDto(
            username = "testuser",
            password = "password123"
        )

        val response = mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
            .andReturn()

        val responseBody = response.response.contentAsString
        val tokenResponse = objectMapper.readTree(responseBody)
        authToken = tokenResponse.get("token").asText()
    }

    @Test
    fun `계좌 생성 성공`() {
        val accountRequest = AccountCreateRequestDto(
            accountName = "테스트 계좌",
            initialBalance = 10000
        )

        mockMvc.perform(
            post("/api/accounts")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.accountNumber").exists())
            .andExpect(jsonPath("$.accountName").value("테스트 계좌"))
            .andExpect(jsonPath("$.balance").value(10000))
    }

    @Test
    fun `계좌 목록 조회 성공`() {
        // 계좌 생성
        val accountRequest = AccountCreateRequestDto(
            accountName = "테스트 계좌",
            initialBalance = 10000
        )

        mockMvc.perform(
            post("/api/accounts")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountRequest))
        )

        // 계좌 목록 조회
        mockMvc.perform(
            get("/api/accounts")
                .header("Authorization", "Bearer $authToken")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].accountName").value("테스트 계좌"))
            .andExpect(jsonPath("$[0].balance").value(10000))
    }

    @Test
    fun `계좌 상세 조회 성공`() {
        // 계좌 생성
        val accountRequest = AccountCreateRequestDto(
            accountName = "테스트 계좌",
            initialBalance = 10000
        )

        val response = mockMvc.perform(
            post("/api/accounts")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountRequest))
        )
            .andReturn()

        val responseBody = response.response.contentAsString
        val accountResponse = objectMapper.readTree(responseBody)
        val accountNumber = accountResponse.get("accountNumber").asText()

        // 계좌 상세 조회
        mockMvc.perform(
            get("/api/accounts/$accountNumber")
                .header("Authorization", "Bearer $authToken")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.accountNumber").value(accountNumber))
            .andExpect(jsonPath("$.accountName").value("테스트 계좌"))
            .andExpect(jsonPath("$.balance").value(10000))
    }
} 