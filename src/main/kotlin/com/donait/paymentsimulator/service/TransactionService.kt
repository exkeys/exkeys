package com.donait.paymentsimulator.service

import com.donait.paymentsimulator.dto.transaction.TransactionDto
import com.donait.paymentsimulator.entity.Account
import com.donait.paymentsimulator.entity.Transaction
import com.donait.paymentsimulator.entity.TransactionStatus
import com.donait.paymentsimulator.repository.AccountRepository
import com.donait.paymentsimulator.repository.TransactionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.NoSuchElementException

@Service
class TransactionService(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
) {
    @Transactional
    fun createTransaction(
        sourceAccountNumber: String,
        targetAccountNumber: String,
        amount: BigDecimal,
        description: String? = null
    ): Transaction {
        val now = LocalDateTime.now()
        val transaction = Transaction(
            sourceAccountNumber = sourceAccountNumber,
            targetAccountNumber = targetAccountNumber,
            amount = amount,
            status = TransactionStatus.PENDING,
            description = description ?: "",
            createdAt = now,
            updatedAt = now
        )
        return transactionRepository.save(transaction)
    }

    @Transactional
    fun processTransaction(transactionId: String): TransactionDto {
        val transaction = transactionRepository.findById(transactionId.toLong())
            .orElseThrow { NoSuchElementException("거래를 찾을 수 없습니다: $transactionId") }

        if (transaction.status != TransactionStatus.PENDING) {
            throw IllegalStateException("이미 처리된 거래입니다: $transactionId")
        }

        val sourceAccount = accountRepository.findByAccountNumber(transaction.sourceAccountNumber)
            .orElseThrow { NoSuchElementException("출금 계좌를 찾을 수 없습니다: ${transaction.sourceAccountNumber}") }
        val targetAccount = accountRepository.findByAccountNumber(transaction.targetAccountNumber)
            .orElseThrow { NoSuchElementException("입금 계좌를 찾을 수 없습니다: ${transaction.targetAccountNumber}") }

        if (sourceAccount.balance.compareTo(transaction.amount) < 0) {
            transaction.status = TransactionStatus.FAILED
            transaction.updatedAt = LocalDateTime.now()
            transactionRepository.save(transaction)
            throw IllegalStateException("잔액이 부족합니다")
        }

        sourceAccount.balance = sourceAccount.balance.subtract(transaction.amount)
        targetAccount.balance = targetAccount.balance.add(transaction.amount)

        accountRepository.save(sourceAccount)
        accountRepository.save(targetAccount)

        transaction.status = TransactionStatus.COMPLETED
        transaction.updatedAt = LocalDateTime.now()
        val savedTransaction = transactionRepository.save(transaction)

        return TransactionDto.fromEntity(savedTransaction)
    }

    @Transactional(readOnly = true)
    fun getTransaction(transactionId: String): TransactionDto {
        val transaction = transactionRepository.findById(transactionId.toLong())
            .orElseThrow { NoSuchElementException("거래를 찾을 수 없습니다: $transactionId") }
        return TransactionDto.fromEntity(transaction)
    }

    @Transactional(readOnly = true)
    fun getAccountTransactions(
        accountNumber: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<TransactionDto> {
        val transactions = transactionRepository.findBySourceAccountNumberOrTargetAccountNumberAndCreatedAtBetween(
            accountNumber,
            accountNumber,
            startDate,
            endDate
        )
        return transactions.map { TransactionDto.fromEntity(it) }
    }
} 