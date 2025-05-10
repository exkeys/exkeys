package com.donait.paymentsimulator.repository

import com.donait.paymentsimulator.model.Account
import com.donait.paymentsimulator.model.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal

@DataJpaTest
@ActiveProfiles("test")
class AccountRepositoryTest {
    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `should save and find account by account number`() {
        // given
        val user = User(username = "testuser", passwordHash = "hashedPassword")
        userRepository.save(user)
        
        val accountNumber = "1234567890"
        val account = Account(
            accountNumber = accountNumber,
            user = user,
            balance = BigDecimal("1000.00")
        )

        // when
        accountRepository.save(account)
        val foundAccount = accountRepository.findByAccountNumber(accountNumber)

        // then
        assertThat(foundAccount).isPresent
        assertThat(foundAccount.get().accountNumber).isEqualTo(accountNumber)
        assertThat(foundAccount.get().user).isEqualTo(user)
        assertThat(foundAccount.get().balance).isEqualTo(BigDecimal("1000.00"))
    }

    @Test
    fun `should find accounts by user`() {
        // given
        val user = User(username = "testuser", passwordHash = "hashedPassword")
        userRepository.save(user)
        
        val account1 = Account(accountNumber = "1234567890", user = user)
        val account2 = Account(accountNumber = "0987654321", user = user)
        
        accountRepository.save(account1)
        accountRepository.save(account2)

        // when
        val userAccounts = accountRepository.findByUser(user)

        // then
        assertThat(userAccounts).hasSize(2)
        assertThat(userAccounts).extracting("accountNumber")
            .containsExactlyInAnyOrder("1234567890", "0987654321")
    }

    @Test
    fun `should check if account number exists`() {
        // given
        val user = User(username = "testuser", passwordHash = "hashedPassword")
        userRepository.save(user)
        
        val accountNumber = "1234567890"
        val account = Account(accountNumber = accountNumber, user = user)
        accountRepository.save(account)

        // when
        val exists = accountRepository.existsByAccountNumber(accountNumber)

        // then
        assertThat(exists).isTrue()
    }

    @Test
    fun `should return false for non-existent account number`() {
        // given
        val accountNumber = "nonexistent"

        // when
        val exists = accountRepository.existsByAccountNumber(accountNumber)

        // then
        assertThat(exists).isFalse()
    }
} 