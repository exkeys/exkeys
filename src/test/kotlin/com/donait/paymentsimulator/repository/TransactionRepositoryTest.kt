package com.donait.paymentsimulator.repository

import com.donait.paymentsimulator.model.Account
import com.donait.paymentsimulator.model.Transaction
import com.donait.paymentsimulator.model.TransactionStatus
import com.donait.paymentsimulator.model.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal

@DataJpaTest
@ActiveProfiles("test")
class TransactionRepositoryTest {
    @Autowired
    private lateinit var transactionRepository: TransactionRepository

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `should save and find transaction by transaction id`() {
        // given
        val user = User(username = "testuser", passwordHash = "hashedPassword")
        userRepository.save(user)
        
        val account = Account(accountNumber = "1234567890", user = user)
        accountRepository.save(account)
        
        val transactionId = "TRX123456"
        val transaction = Transaction(
            transactionId = transactionId,
            account = account,
            amount = BigDecimal("100.00"),
            status = TransactionStatus.PENDING
        )

        // when
        transactionRepository.save(transaction)
        val foundTransaction = transactionRepository.findByTransactionId(transactionId)

        // then
        assertThat(foundTransaction).isPresent
        assertThat(foundTransaction.get().transactionId).isEqualTo(transactionId)
        assertThat(foundTransaction.get().account).isEqualTo(account)
        assertThat(foundTransaction.get().amount).isEqualTo(BigDecimal("100.00"))
        assertThat(foundTransaction.get().status).isEqualTo(TransactionStatus.PENDING)
    }

    @Test
    fun `should find transactions by account`() {
        // given
        val user = User(username = "testuser", passwordHash = "hashedPassword")
        userRepository.save(user)
        
        val account = Account(accountNumber = "1234567890", user = user)
        accountRepository.save(account)
        
        val transaction1 = Transaction(
            transactionId = "TRX123456",
            account = account,
            amount = BigDecimal("100.00")
        )
        val transaction2 = Transaction(
            transactionId = "TRX789012",
            account = account,
            amount = BigDecimal("200.00")
        )
        
        transactionRepository.save(transaction1)
        transactionRepository.save(transaction2)

        // when
        val accountTransactions = transactionRepository.findByAccount(account)

        // then
        assertThat(accountTransactions).hasSize(2)
        assertThat(accountTransactions).extracting("transactionId")
            .containsExactlyInAnyOrder("TRX123456", "TRX789012")
    }

    @Test
    fun `should find transactions by status`() {
        // given
        val user = User(username = "testuser", passwordHash = "hashedPassword")
        userRepository.save(user)
        
        val account = Account(accountNumber = "1234567890", user = user)
        accountRepository.save(account)
        
        val pendingTransaction = Transaction(
            transactionId = "TRX123456",
            account = account,
            amount = BigDecimal("100.00"),
            status = TransactionStatus.PENDING
        )
        val completedTransaction = Transaction(
            transactionId = "TRX789012",
            account = account,
            amount = BigDecimal("200.00"),
            status = TransactionStatus.COMPLETED
        )
        
        transactionRepository.save(pendingTransaction)
        transactionRepository.save(completedTransaction)

        // when
        val pendingTransactions = transactionRepository.findByStatus(TransactionStatus.PENDING)

        // then
        assertThat(pendingTransactions).hasSize(1)
        assertThat(pendingTransactions[0].transactionId).isEqualTo("TRX123456")
        assertThat(pendingTransactions[0].status).isEqualTo(TransactionStatus.PENDING)
    }
} 