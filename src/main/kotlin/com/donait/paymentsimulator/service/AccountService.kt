package com.donait.paymentsimulator.service

import com.donait.paymentsimulator.dto.account.AccountCreateRequestDto
import com.donait.paymentsimulator.dto.account.AccountResponseDto
import com.donait.paymentsimulator.entity.Account
import com.donait.paymentsimulator.entity.User
import com.donait.paymentsimulator.repository.AccountRepository
import com.donait.paymentsimulator.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.format.DateTimeFormatter
import java.util.UUID
import java.util.NoSuchElementException

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val userRepository: UserRepository
) {
    @Transactional
    fun createAccount(userId: Long, request: AccountCreateRequestDto): AccountResponseDto {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("사용자를 찾을 수 없습니다.") }

        val account = Account(
            accountNumber = generateAccountNumber(),
            accountName = request.accountName,
            balance = request.initialBalance,
            user = user
        )

        val savedAccount = accountRepository.save(account)
        return convertToResponseDto(savedAccount)
    }

    @Transactional(readOnly = true)
    fun getAccountsByUserId(userId: Long): List<AccountResponseDto> {
        return accountRepository.findByUserId(userId)
            .map { convertToResponseDto(it) }
    }

    @Transactional(readOnly = true)
    fun getAccountByAccountNumber(accountNumber: String): AccountResponseDto {
        val account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow { IllegalArgumentException("계좌를 찾을 수 없습니다.") }
        return convertToResponseDto(account)
    }

    @Transactional
    fun updateBalance(accountNumber: String, amount: BigDecimal) {
        val account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow { IllegalArgumentException("계좌를 찾을 수 없습니다.") }
        
        account.balance = account.balance.add(amount)
        accountRepository.save(account)
    }

    private fun generateAccountNumber(): String {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12)
    }

    private fun convertToResponseDto(account: Account): AccountResponseDto {
        return AccountResponseDto(
            accountNumber = account.accountNumber,
            accountName = account.accountName,
            balance = account.balance,
            createdAt = account.createdAt.format(DateTimeFormatter.ISO_DATE_TIME)
        )
    }
} 