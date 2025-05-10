package com.donait.paymentsimulator.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "accounts")
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(unique = true, nullable = false)
    val accountNumber: String,

    @Column(nullable = false)
    val accountName: String,

    @Column(nullable = false)
    var balance: BigDecimal,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @OneToMany(mappedBy = "sourceAccount")
    val outgoingTransactions: MutableList<Transaction> = mutableListOf(),

    @OneToMany(mappedBy = "targetAccount")
    val incomingTransactions: MutableList<Transaction> = mutableListOf()
) 