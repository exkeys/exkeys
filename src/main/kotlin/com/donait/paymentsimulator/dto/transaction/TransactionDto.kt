package com.donait.paymentsimulator.dto.transaction

import com.donait.paymentsimulator.entity.Transaction
import com.donait.paymentsimulator.entity.TransactionStatus
import java.math.BigDecimal
import java.time.LocalDateTime
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class TransactionRequestDto(
    @field:NotBlank(message = "출금 계좌번호는 필수입니다")
    val sourceAccountNumber: String,

    @field:NotBlank(message = "입금 계좌번호는 필수입니다")
    val targetAccountNumber: String,

    @field:NotNull(message = "거래 금액은 필수입니다")
    @field:Positive(message = "거래 금액은 0보다 커야 합니다")
    val amount: BigDecimal,

    val description: String? = null
)

data class TransactionResponseDto(
    val transactionId: String,
    val sourceAccountNumber: String,
    val targetAccountNumber: String,
    val amount: BigDecimal,
    val status: String,
    val description: String?,
    val createdAt: String
)

data class TransactionDto(
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
        fun fromEntity(transaction: Transaction) = TransactionDto(
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

data class TransactionListResponseDto(
    val transactions: List<TransactionDto>,
    val totalCount: Int
) 