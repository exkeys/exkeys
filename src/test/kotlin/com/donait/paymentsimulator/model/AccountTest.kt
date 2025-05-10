package com.donait.paymentsimulator.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class AccountTest {
    @Test
    fun `should create account with valid data`() {
        // given
        val accountNumber = "1234567890"
        val user = User(username = "testuser", passwordHash = "hashedPassword")
        val balance = BigDecimal("1000.00")
        val createdAt = LocalDateTime.now()

        // when
        val account = Account(
            accountNumber = accountNumber,
            user = user,
            balance = balance,
            createdAt = createdAt
        )

        // then
        assertThat(account.id).isEqualTo(0)
        assertThat(account.accountNumber).isEqualTo(accountNumber)
        assertThat(account.user).isEqualTo(user)
        assertThat(account.balance).isEqualTo(balance)
        assertThat(account.createdAt).isEqualTo(createdAt)
    }

    @Test
    fun `should create account with default balance and created at time`() {
        // given
        val accountNumber = "1234567890"
        val user = User(username = "testuser", passwordHash = "hashedPassword")

        // when
        val account = Account(
            accountNumber = accountNumber,
            user = user
        )

        // then
        assertThat(account.id).isEqualTo(0)
        assertThat(account.accountNumber).isEqualTo(accountNumber)
        assertThat(account.user).isEqualTo(user)
        assertThat(account.balance).isEqualTo(BigDecimal.ZERO)
        assertThat(account.createdAt).isNotNull()
    }
} 