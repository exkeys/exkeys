package com.donait.paymentsimulator.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "transactions")
data class Transaction(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String = "",

    @Column(name = "source_account_number", nullable = false)
    val sourceAccountNumber: String,

    @Column(name = "target_account_number", nullable = false)
    val targetAccountNumber: String,

    @Column(nullable = false)
    val amount: BigDecimal,

    @Column(nullable = false)
    val description: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: TransactionStatus,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime
) 