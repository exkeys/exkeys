package com.donait.paymentsimulator.dto

import com.donait.paymentsimulator.model.TransactionStatus
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.time.LocalDateTime

data class PaymentInitRequestDto(
    @field:NotBlank(message = "계좌번호는 필수입니다")
    val accountNumber: String,

    @field:NotBlank(message = "금액은 필수입니다")
    @field:Positive(message = "금액은 0보다 커야 합니다")
    val amount: String
)

data class PaymentConfirmRequestDto(
    @field:NotBlank(message = "거래 ID는 필수입니다")
    val transactionId: String
)

data class PaymentStatusResponseDto(
    val transactionId: String,
    val accountNumber: String,
    val amount: BigDecimal,
    val status: TransactionStatus,
    val createdAt: LocalDateTime
) 