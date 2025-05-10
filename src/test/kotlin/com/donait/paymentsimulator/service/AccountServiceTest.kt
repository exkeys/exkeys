package com.donait.paymentsimulator.service

import com.donait.paymentsimulator.model.Account
import com.donait.paymentsimulator.model.User
import com.donait.paymentsimulator.repository.AccountRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.util.NoSuchElementException

@ExtendWith(MockitoExtension::class)
class AccountServiceTest {

    @Mock
    private lateinit var accountRepository: AccountRepository

    @Mock
    private lateinit var userService: UserService

    @InjectMocks
    private lateinit var accountService: AccountService

    @Test
    fun `should create account successfully`() {
        // given
        val username = "testuser"
        val user = User(username = username, passwordHash = "password")
        val account = Account(
            accountNumber = "1234567890",
            user = user,
            balance = BigDecimal.ZERO
        )

        `when`(userService.findByUsername(username)).thenReturn(user)
        `when`(accountRepository.save(any(Account::class.java))).thenReturn(account)

        // when
        val result = accountService.createAccount(username)

        // then
        assertThat(result.accountNumber).isEqualTo(account.accountNumber)
        assertThat(result.user).isEqualTo(user)
        assertThat(result.balance).isEqualTo(BigDecimal.ZERO)
        verify(accountRepository).save(any(Account::class.java))
    }

    @Test
    fun `should get account by account number`() {
        // given
        val accountNumber = "1234567890"
        val account = Account(
            accountNumber = accountNumber,
            user = User(username = "testuser", passwordHash = "password"),
            balance = BigDecimal.ZERO
        )

        `when`(accountRepository.findByAccountNumber(accountNumber)).thenReturn(account)

        // when
        val result = accountService.getAccount(accountNumber)

        // then
        assertThat(result).isEqualTo(account)
    }

    @Test
    fun `should throw exception when account not found`() {
        // given
        val accountNumber = "1234567890"

        `when`(accountRepository.findByAccountNumber(accountNumber)).thenReturn(null)

        // when & then
        assertThatThrownBy { accountService.getAccount(accountNumber) }
            .isInstanceOf(NoSuchElementException::class.java)
            .hasMessage("계좌를 찾을 수 없습니다.")
    }

    @Test
    fun `should update balance successfully`() {
        // given
        val accountNumber = "1234567890"
        val newBalance = BigDecimal("1000.00")
        val account = Account(
            accountNumber = accountNumber,
            user = User(username = "testuser", passwordHash = "password"),
            balance = BigDecimal.ZERO
        )

        `when`(accountRepository.findByAccountNumber(accountNumber)).thenReturn(account)
        `when`(accountRepository.save(any(Account::class.java))).thenAnswer { it.arguments[0] }

        // when
        val result = accountService.updateBalance(accountNumber, newBalance)

        // then
        assertThat(result.balance).isEqualTo(newBalance)
        verify(accountRepository).save(any(Account::class.java))
    }
} 