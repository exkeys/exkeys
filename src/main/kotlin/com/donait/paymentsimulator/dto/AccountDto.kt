package com.donait.paymentsimulator.dto

import java.math.BigDecimal
import java.time.LocalDateTime

data class AccountResponseDto(
    val accountNumber: String,
    val balance: BigDecimal,
    val createdAt: LocalDateTime
)

data class AccountListResponseDto(
    val accounts: List<AccountResponseDto>
) 