package com.donait.paymentsimulator.repository

import com.donait.paymentsimulator.entity.Account
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface AccountRepository : JpaRepository<Account, Long> {
    fun findByAccountNumber(accountNumber: String): Optional<Account>
    fun findByUserId(userId: Long): List<Account>
} 