package com.donait.paymentsimulator.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class TransactionTest {
    @Test
    fun `should create transaction with valid data`() {
        // given
        val transactionId = "TRX123456"
        val account = Account(
            accountNumber = "1234567890",
            user = User(username = "testuser", passwordHash = "hashedPassword")
        )
        val amount = BigDecimal("100.00")
        val status = TransactionStatus.PENDING
        val createdAt = LocalDateTime.now()

        // when
        val transaction = Transaction(
            transactionId = transactionId,
            account = account,
            amount = amount,
            status = status,
            createdAt = createdAt
        )

        // then
        assertThat(transaction.id).isEqualTo(0)
        assertThat(transaction.transactionId).isEqualTo(transactionId)
        assertThat(transaction.account).isEqualTo(account)
        assertThat(transaction.amount).isEqualTo(amount)
        assertThat(transaction.status).isEqualTo(status)
        assertThat(transaction.createdAt).isEqualTo(createdAt)
    }

    @Test
    fun `should create transaction with default status and created at time`() {
        // given
        val transactionId = "TRX123456"
        val account = Account(
            accountNumber = "1234567890",
            user = User(username = "testuser", passwordHash = "hashedPassword")
        )
        val amount = BigDecimal("100.00")

        // when
        val transaction = Transaction(
            transactionId = transactionId,
            account = account,
            amount = amount
        )

        // then
        assertThat(transaction.id).isEqualTo(0)
        assertThat(transaction.transactionId).isEqualTo(transactionId)
        assertThat(transaction.account).isEqualTo(account)
        assertThat(transaction.amount).isEqualTo(amount)
        assertThat(transaction.status).isEqualTo(TransactionStatus.PENDING)
        assertThat(transaction.createdAt).isNotNull()
    }
} 