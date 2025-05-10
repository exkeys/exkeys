package com.donait.paymentsimulator.dto.account

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class AccountCreateRequestDto(
    @field:NotBlank(message = "계좌 이름은 필수입니다")
    val accountName: String,

    @field:NotNull(message = "초기 잔액은 필수입니다")
    @field:Positive(message = "초기 잔액은 0보다 커야 합니다")
    val initialBalance: BigDecimal
)

data class AccountResponseDto(
    val accountNumber: String,
    val accountName: String,
    val balance: BigDecimal,
    val createdAt: String
)

data class AccountListResponseDto(
    val accounts: List<AccountResponseDto>
) 