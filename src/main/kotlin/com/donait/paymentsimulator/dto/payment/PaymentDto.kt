package com.donait.paymentsimulator.dto.payment

import com.donait.paymentsimulator.entity.Transaction
import com.donait.paymentsimulator.entity.TransactionStatus
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDateTime

data class PaymentInitiateRequestDto(
    @field:NotBlank(message = "출금 계좌번호는 필수입니다")
    val sourceAccountNumber: String,

    @field:NotBlank(message = "입금 계좌번호는 필수입니다")
    val targetAccountNumber: String,

    @field:NotNull(message = "금액은 필수입니다")
    @field:DecimalMin(value = "0.01", message = "금액은 0보다 커야 합니다")
    val amount: BigDecimal,

    @field:NotBlank(message = "설명은 필수입니다")
    val description: String
)

data class PaymentConfirmRequestDto(
    val transactionId: String
)

data class PaymentStatusResponseDto(
    val id: String,
    val sourceAccountNumber: String,
    val targetAccountNumber: String,
    val amount: BigDecimal,
    val status: TransactionStatus,
    val description: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun fromEntity(transaction: Transaction) = PaymentStatusResponseDto(
            id = transaction.id.toString(),
            sourceAccountNumber = transaction.sourceAccountNumber,
            targetAccountNumber = transaction.targetAccountNumber,
            amount = transaction.amount,
            status = transaction.status,
            description = transaction.description,
            createdAt = transaction.createdAt,
            updatedAt = transaction.updatedAt
        )
    }
} 