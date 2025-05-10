package com.donait.paymentsimulator.service

import com.donait.paymentsimulator.model.Account
import com.donait.paymentsimulator.model.Transaction
import com.donait.paymentsimulator.model.TransactionStatus
import com.donait.paymentsimulator.model.User
import com.donait.paymentsimulator.repository.TransactionRepository
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
class TransactionServiceTest {

    @Mock
    private lateinit var transactionRepository: TransactionRepository

    @Mock
    private lateinit var accountService: AccountService

    @InjectMocks
    private lateinit var transactionService: TransactionService

    @Test
    fun `should create transaction successfully`() {
        // given
        val accountNumber = "1234567890"
        val amount = BigDecimal("1000.00")
        val account = Account(
            accountNumber = accountNumber,
            user = User(username = "testuser", passwordHash = "password"),
            balance = BigDecimal("2000.00")
        )
        val transaction = Transaction(
            transactionId = "TRX123",
            account = account,
            amount = amount
        )

        `when`(accountService.getAccount(accountNumber)).thenReturn(account)
        `when`(transactionRepository.save(any(Transaction::class.java))).thenReturn(transaction)

        // when
        val result = transactionService.createTransaction(accountNumber, amount)

        // then
        assertThat(result.transactionId).isEqualTo(transaction.transactionId)
        assertThat(result.account).isEqualTo(account)
        assertThat(result.amount).isEqualTo(amount)
        assertThat(result.status).isEqualTo(TransactionStatus.PENDING)
        verify(transactionRepository).save(any(Transaction::class.java))
    }

    @Test
    fun `should process transaction successfully`() {
        // given
        val transactionId = "TRX123"
        val accountNumber = "1234567890"
        val amount = BigDecimal("1000.00")
        val account = Account(
            accountNumber = accountNumber,
            user = User(username = "testuser", passwordHash = "password"),
            balance = BigDecimal("2000.00")
        )
        val transaction = Transaction(
            transactionId = transactionId,
            account = account,
            amount = amount
        )

        `when`(transactionRepository.findByTransactionId(transactionId)).thenReturn(transaction)
        `when`(accountService.updateBalance(eq(accountNumber), any())).thenReturn(account)
        `when`(transactionRepository.save(any(Transaction::class.java))).thenAnswer { it.arguments[0] }

        // when
        val result = transactionService.processTransaction(transactionId)

        // then
        assertThat(result.status).isEqualTo(TransactionStatus.COMPLETED)
        verify(accountService).updateBalance(eq(accountNumber), eq(account.balance - amount))
        verify(transactionRepository).save(any(Transaction::class.java))
    }

    @Test
    fun `should throw exception when transaction not found`() {
        // given
        val transactionId = "TRX123"

        `when`(transactionRepository.findByTransactionId(transactionId)).thenReturn(null)

        // when & then
        assertThatThrownBy { transactionService.getTransaction(transactionId) }
            .isInstanceOf(NoSuchElementException::class.java)
            .hasMessage("거래를 찾을 수 없습니다.")
    }

    @Test
    fun `should throw exception when processing already processed transaction`() {
        // given
        val transactionId = "TRX123"
        val transaction = Transaction(
            transactionId = transactionId,
            account = Account(
                accountNumber = "1234567890",
                user = User(username = "testuser", passwordHash = "password"),
                balance = BigDecimal("2000.00")
            ),
            amount = BigDecimal("1000.00"),
            status = TransactionStatus.COMPLETED
        )

        `when`(transactionRepository.findByTransactionId(transactionId)).thenReturn(transaction)

        // when & then
        assertThatThrownBy { transactionService.processTransaction(transactionId) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessage("이미 처리된 거래입니다.")
    }

    @Test
    fun `should throw exception when insufficient balance`() {
        // given
        val transactionId = "TRX123"
        val amount = BigDecimal("3000.00")
        val account = Account(
            accountNumber = "1234567890",
            user = User(username = "testuser", passwordHash = "password"),
            balance = BigDecimal("2000.00")
        )
        val transaction = Transaction(
            transactionId = transactionId,
            account = account,
            amount = amount
        )

        `when`(transactionRepository.findByTransactionId(transactionId)).thenReturn(transaction)
        `when`(transactionRepository.save(any(Transaction::class.java))).thenAnswer { it.arguments[0] }

        // when & then
        assertThatThrownBy { transactionService.processTransaction(transactionId) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessage("잔액이 부족합니다.")
    }
} 