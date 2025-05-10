package com.donait.paymentsimulator.repository

import com.donait.paymentsimulator.entity.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository : JpaRepository<Transaction, String> {
    fun findBySourceAccountNumberOrTargetAccountNumber(
        sourceAccountNumber: String,
        targetAccountNumber: String
    ): List<Transaction>
} 