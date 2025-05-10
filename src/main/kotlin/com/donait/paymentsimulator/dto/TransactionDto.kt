package com.donait.paymentsimulator.dto

import com.donait.paymentsimulator.model.TransactionStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionRequestDto(
    val accountNumber: String,
    val amount: String
)

data class TransactionResponseDto(
    val transactionId: String,
    val accountNumber: String,
    val amount: BigDecimal,
    val status: TransactionStatus,
    val createdAt: LocalDateTime
) 