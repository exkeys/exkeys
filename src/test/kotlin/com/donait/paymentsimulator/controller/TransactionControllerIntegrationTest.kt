package com.donait.paymentsimulator.controller

import com.donait.paymentsimulator.IntegrationTest
import com.donait.paymentsimulator.dto.SignupRequestDto
import com.donait.paymentsimulator.dto.LoginRequestDto
import com.donait.paymentsimulator.dto.AccountCreateRequestDto
import com.donait.paymentsimulator.dto.TransactionRequestDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal

class TransactionControllerIntegrationTest : IntegrationTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private var authToken: String = ""
    private var sourceAccountNumber: String = ""
    private var targetAccountNumber: String = ""

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

        // 출금 계좌 생성
        val sourceAccountRequest = AccountCreateRequestDto(
            accountName = "출금 계좌",
            initialBalance = 10000
        )

        val sourceResponse = mockMvc.perform(
            post("/api/accounts")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sourceAccountRequest))
        )
            .andReturn()

        val sourceResponseBody = sourceResponse.response.contentAsString
        val sourceAccountResponse = objectMapper.readTree(sourceResponseBody)
        sourceAccountNumber = sourceAccountResponse.get("accountNumber").asText()

        // 입금 계좌 생성
        val targetAccountRequest = AccountCreateRequestDto(
            accountName = "입금 계좌",
            initialBalance = 0
        )

        val targetResponse = mockMvc.perform(
            post("/api/accounts")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(targetAccountRequest))
        )
            .andReturn()

        val targetResponseBody = targetResponse.response.contentAsString
        val targetAccountResponse = objectMapper.readTree(targetResponseBody)
        targetAccountNumber = targetAccountResponse.get("accountNumber").asText()
    }

    @Test
    fun `거래 생성 성공`() {
        val transactionRequest = TransactionRequestDto(
            sourceAccountNumber = sourceAccountNumber,
            targetAccountNumber = targetAccountNumber,
            amount = BigDecimal("5000"),
            description = "테스트 거래"
        )

        mockMvc.perform(
            post("/api/transactions")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.transactionId").exists())
            .andExpect(jsonPath("$.status").value("COMPLETED"))
    }

    @Test
    fun `잔액 부족으로 거래 실패`() {
        val transactionRequest = TransactionRequestDto(
            sourceAccountNumber = sourceAccountNumber,
            targetAccountNumber = targetAccountNumber,
            amount = BigDecimal("15000"),
            description = "잔액 부족 테스트"
        )

        mockMvc.perform(
            post("/api/transactions")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionRequest))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message").value("잔액이 부족합니다."))
    }

    @Test
    fun `거래 내역 조회 성공`() {
        // 거래 생성
        val transactionRequest = TransactionRequestDto(
            sourceAccountNumber = sourceAccountNumber,
            targetAccountNumber = targetAccountNumber,
            amount = BigDecimal("5000"),
            description = "테스트 거래"
        )

        mockMvc.perform(
            post("/api/transactions")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionRequest))
        )

        // 거래 내역 조회
        mockMvc.perform(
            get("/api/transactions")
                .header("Authorization", "Bearer $authToken")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].sourceAccountNumber").value(sourceAccountNumber))
            .andExpect(jsonPath("$[0].targetAccountNumber").value(targetAccountNumber))
            .andExpect(jsonPath("$[0].amount").value(5000))
            .andExpect(jsonPath("$[0].status").value("COMPLETED"))
    }
} 